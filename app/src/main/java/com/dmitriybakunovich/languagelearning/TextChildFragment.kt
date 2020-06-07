package com.dmitriybakunovich.languagelearning

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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

        viewModel.textSelected.observe(viewLifecycleOwner, Observer {
            Log.v("QQQQQQQQQQ", "selected = $it")
            //  viewModel.selectionString(textBook, firstElement, lastElement)
        })
        viewModel.textLineSelected.observe(viewLifecycleOwner, Observer {
            val text = textChild.text.toString()
            textChild.setText(viewModel.handleLineSelected(text, it), TextView.BufferType.SPANNABLE)
        })
    }
}