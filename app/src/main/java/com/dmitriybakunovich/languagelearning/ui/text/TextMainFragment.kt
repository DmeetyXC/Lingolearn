package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.dmitriybakunovich.languagelearning.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.text_main_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TextMainFragment : Fragment() {

    private val viewModel: TextViewModel by sharedViewModel()

    // Detect long click time
    private var startClickTime: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.text_main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeView()
        registerTouchListener()
        longClickDictionary()
    }

    private fun observeView() {
        viewModel.bookPage.observe(viewLifecycleOwner, {
            textMain.text = it.textMain
        })
        viewModel.textLineSelected.observe(viewLifecycleOwner, {
            val text = textMain.text.toString()
            textMain.setText(viewModel.handleLineSelected(text, it), TextView.BufferType.SPANNABLE)
        })
        viewModel.scrollTextState.observe(viewLifecycleOwner, {
            scrollMain.scrollBy(0, it)
        })
        viewModel.dictionaryModeState.observe(viewLifecycleOwner, {
            dictionaryModeState(it)
        })
        viewModel.textSizeState.observe(viewLifecycleOwner, {
            textMain.textSize = it
        })
    }

    private fun registerTouchListener() {
        textMain.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            return@OnTouchListener textTouchListen(v, event)
        })
    }

    private fun textTouchListen(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startClickTime = System.currentTimeMillis()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if ((System.currentTimeMillis() - startClickTime) > 500) {
                    viewModel.dictionaryModeState(true)
                } else {
                    (v as TextView).layout?.let {
                        val line = it.getLineForVertical(event.y.toInt())
                        val offset = it.getOffsetForHorizontal(line, event.x)
                        val text = textMain.text.toString()
                        viewModel.touchText(offset, text, BookType.MAIN)
                        viewModel.searchNumberLineText(offset, text)
                        viewModel.scrollTextPosition(it.lineCount / 2, line)
                    }
                }
                return true
            }
            else -> false
        }
    }

    private fun dictionaryModeState(state: Boolean) {
        if (state) {
            showSnackBar()
            with(textMain) {
                setTextIsSelectable(true)
                setOnTouchListener(null)
                text = textMain.text.toString()
            }
        } else {
            textMain.setTextIsSelectable(false)
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
        textMain.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

            override fun onDestroyActionMode(mode: ActionMode?) {}

            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                mode?.menuInflater?.inflate(R.menu.text_dictionary, menu)
                return true
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                if (item.itemId == R.id.addTextDictionary) {
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
        val textAll = textMain.text
        var max: Int = textAll.length
        if (textMain.isFocused) {
            val selStart: Int = textMain.selectionStart
            val selEnd: Int = textMain.selectionEnd
            min = 0.coerceAtLeast(selStart.coerceAtMost(selEnd))
            max = 0.coerceAtLeast(selStart.coerceAtLeast(selEnd))
        }
        viewModel.textDictionarySearch(textAll.toString(), min, max)
    }
}
