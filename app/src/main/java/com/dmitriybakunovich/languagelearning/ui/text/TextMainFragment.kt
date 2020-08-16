package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.dmitriybakunovich.languagelearning.R
import kotlinx.android.synthetic.main.text_main_fragment.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class TextMainFragment : Fragment() {

    companion object {
        fun newInstance() = TextMainFragment()
    }

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
        viewModel.bookPage.observe(viewLifecycleOwner, Observer {
            textMain.text = it.textMain
        })
        viewModel.textLineSelected.observe(viewLifecycleOwner, Observer {
            val text = textMain.text.toString()
            textMain.setText(viewModel.handleLineSelected(text, it), TextView.BufferType.SPANNABLE)
        })
        viewModel.scrollTextState.observe(viewLifecycleOwner, Observer {
            scrollMain.scrollBy(0, it)
        })
        viewModel.dictionaryModeState.observe(viewLifecycleOwner, Observer {
            dictionaryModeState(it)
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
            Toast.makeText(
                requireActivity(),
                requireActivity().getString(R.string.text_mode_selected),
                Toast.LENGTH_SHORT
            ).show()
            textMain.setTextIsSelectable(true)
            textMain.setOnTouchListener(null)
            textMain.text = textMain.text.toString()
        } else {
            Toast.makeText(
                requireActivity(),
                requireActivity().getString(R.string.text_mode_disabled),
                Toast.LENGTH_SHORT
            ).show()
            textMain.setTextIsSelectable(false)
            registerTouchListener()
        }
    }

    private fun longClickDictionary() {
        textMain.customSelectionActionModeCallback = object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean = false

            override fun onDestroyActionMode(mode: ActionMode?) {
                viewModel.dictionaryModeState(false)
            }

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
