package com.dmitriybakunovich.languagelearning

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.book_fragment.*


class BookFragment : Fragment() {

    companion object {
        fun newInstance() = BookFragment()
    }

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.book_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(BookViewModel::class.java)
        // TODO: Use the ViewModel
        textBook.setOnTouchListener(View.OnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val layout = (v as TextView).layout
                    val x = event.x.toInt()
                    val y = event.y.toInt()
                    if (layout != null) {
                        val line = layout.getLineForVertical(y)
                        val offset = layout.getOffsetForHorizontal(line, x.toFloat())

                        val firstElement = viewModel.searchFirstElement(offset, textBook.text.toString())
                        val lastElement = viewModel.searchLastElement(offset, textBook.text.toString())
                        val texts = textBook.text.subSequence(firstElement, lastElement)
                        Log.v("QQQQQQQQQ", "result = $texts")
                        viewModel.highlightString(textBook, firstElement, lastElement)
                    }
                    return@OnTouchListener true
                }
                else -> false
            }
        })
    }
}