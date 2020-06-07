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
import kotlinx.android.synthetic.main.text_main_fragment.*

class TextMainFragment : Fragment() {

    companion object {
        fun newInstance() = TextMainFragment()
    }

    private val viewModel: TextViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.text_main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.textSelected.observe(viewLifecycleOwner, Observer {
            textMain.setText(it, TextView.BufferType.SPANNABLE)
        })
        textMain.setOnTouchListener(View.OnTouchListener { v, event ->
            v.performClick()
            return@OnTouchListener textTouchListen(v, event)
        })
    }

    private fun textTouchListen(v: View, event: MotionEvent): Boolean {
        return when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val layout = (v as TextView).layout
                val x = event.x.toInt()
                val y = event.y.toInt()
                if (layout != null) {
                    val line = layout.getLineForVertical(y)
                    val offset = layout.getOffsetForHorizontal(line, x.toFloat())
                    val text = textMain.text.toString()
                    viewModel.touchText(offset, text)
                    viewModel.searchNumberLineText(offset, text)
                }
                return true
            }
            else -> false
        }
    }
}
