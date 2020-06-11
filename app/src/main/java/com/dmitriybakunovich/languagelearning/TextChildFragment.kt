package com.dmitriybakunovich.languagelearning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.text_child_fragment.*

class TextChildFragment : Fragment() {

    companion object {
        fun newInstance() = TextChildFragment()
    }

    private val viewModel: TextViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.text_child_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeView()
        textChild.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            return@OnTouchListener textTouchListen(v, event)
        })
    }

    private fun observeView() {
        viewModel.allText.observe(viewLifecycleOwner, Observer {
            if (it.isNotEmpty()) textChild.text = it[0].textChild
        })
        viewModel.textLineSelected.observe(viewLifecycleOwner, Observer {
            val text = textChild.text.toString()
            textChild.setText(viewModel.handleLineSelected(text, it), TextView.BufferType.SPANNABLE)
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
                if (layout != null) {
                    val line = layout.getLineForVertical(y)
                    val offset = layout.getOffsetForHorizontal(line, x.toFloat())
                    val text = textChild.text.toString()
                    viewModel.touchText(offset, text, TextTouchType.CHILD)
                    viewModel.searchNumberLineText(offset, text)

                    // Scroll down
                    val lineCountMax = layout.lineCount
                    val lineTwain = lineCountMax / 2
                    if (lineTwain <= line + 2) {
                        scrollChild.scrollBy(0, lineTwain + 100)
                    }
                }
                return true
            }
            else -> false
        }
    }
}