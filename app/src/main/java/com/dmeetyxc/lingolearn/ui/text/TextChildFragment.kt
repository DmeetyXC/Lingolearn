package com.dmeetyxc.lingolearn.ui.text

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.FragmentTextChildBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TextChildFragment : Fragment(R.layout.fragment_text_child) {

    private val viewModel: TextViewModel by sharedViewModel()
    private var _binding: FragmentTextChildBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentTextChildBinding.bind(view)

        observeView()
        registerTouchListener()
        longClickDictionary()
    }

    private fun observeView() {
        viewModel.bookPage.observe(viewLifecycleOwner, {
            binding.textChild.text = it.textChild
        })
        viewModel.textLineSelected.observe(viewLifecycleOwner, {
            val text = binding.textChild.text.toString()
            binding.textChild.setText(
                viewModel.handleLineSelected(text, it), TextView.BufferType.SPANNABLE
            )
        })
        viewModel.scrollTextState.observe(viewLifecycleOwner, {
            binding.scrollChild.scrollBy(0, it)
        })
        viewModel.dictionaryModeState.observe(viewLifecycleOwner, {
            dictionaryModeState(it)
        })
        viewModel.textSizeState.observe(viewLifecycleOwner, {
            binding.textChild.textSize = it
        })
    }

    private fun registerTouchListener() {
        val gestureDetector = GestureDetectorCompat(requireContext(), TextGestureListener {
            when (it) {
                is TextGestureListener.TypeTouchText.SingleTap -> touchText(it.event)
                is TextGestureListener.TypeTouchText.LongPress ->
                    viewModel.dictionaryModeState(true)
            }
        })

        binding.textChild.setOnTouchListener { v, event ->
            v.performClick()
            gestureDetector.onTouchEvent(event)
        }
    }

    private fun touchText(event: MotionEvent) {
        binding.textChild.layout?.let {
            val line = it.getLineForVertical(event.y.toInt())
            val offset = it.getOffsetForHorizontal(line, event.x)
            val text = binding.textChild.text.toString()
            viewModel.touchText(offset, text, BookType.CHILD)
            viewModel.searchNumberLineText(offset, text)
            viewModel.scrollTextPosition(it.lineCount / 2, line)
        }
    }

    private fun dictionaryModeState(state: Boolean) {
        if (state) {
            showSnackBar()
            with(binding.textChild) {
                setTextIsSelectable(true)
                setOnTouchListener(null)
                text = binding.textChild.text.toString()
            }
        } else {
            binding.textChild.setTextIsSelectable(false)
            registerTouchListener()
        }
    }

    private fun showSnackBar() {
        Snackbar.make(
            requireView(), requireActivity().getString(R.string.text_mode_selected),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(R.string.cancel) { viewModel.dictionaryModeState(false) }
            .show()
    }

    private fun longClickDictionary() {
        binding.textChild.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

            override fun onDestroyActionMode(mode: ActionMode?) {}

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.text_dictionary, menu)
                return true
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                if (item.itemId == R.id.add_text_dictionary) {
                    textDictionary()
                    mode.finish()
                    return true
                }
                return false
            }
        }
    }

    private fun textDictionary() {
        var min = 0
        val textAll = binding.textChild.text
        var max: Int = textAll.length
        if (binding.textChild.isFocused) {
            val selStart: Int = binding.textChild.selectionStart
            val selEnd: Int = binding.textChild.selectionEnd
            min = 0.coerceAtLeast(selStart.coerceAtMost(selEnd))
            max = 0.coerceAtLeast(selStart.coerceAtLeast(selEnd))
        }
        viewModel.textDictionarySearch(textAll.toString(), min, max)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}