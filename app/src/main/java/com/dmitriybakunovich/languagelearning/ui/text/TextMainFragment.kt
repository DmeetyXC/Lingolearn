package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.text_main_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeView()
        textMain.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            return@OnTouchListener textTouchListen(v, event)
        })
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
    }

    private fun textTouchListen(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                return true
            }
            MotionEvent.ACTION_UP -> {
                val layout = (v as TextView).layout
                val x = event.x.toInt()
                val y = event.y.toInt()
                layout?.let {
                    val line = it.getLineForVertical(y)
                    val offset = it.getOffsetForHorizontal(line, x.toFloat())
                    val text = textMain.text.toString()
                    viewModel.touchText(offset, text, TextTouchType.MAIN)
                    viewModel.searchNumberLineText(offset, text)
                    viewModel.scrollTextPosition(it.lineCount / 2, line)
                }
                return true
            }
            else -> false
        }
    }
}
