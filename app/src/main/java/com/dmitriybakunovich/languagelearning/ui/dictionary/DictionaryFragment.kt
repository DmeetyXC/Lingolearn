package com.dmitriybakunovich.languagelearning.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import kotlinx.android.synthetic.main.dictionary_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DictionaryFragment : Fragment() {

    private val viewModel: DictionaryViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dictionary_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerDictionary.layoutManager = LinearLayoutManager(requireActivity())
        observerView()
    }

    private fun observerView() {
        viewModel.dictionary.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                txtEmptyDictionary.visibility = View.VISIBLE
            } else {
                recyclerDictionary.adapter = DictionaryAdapter(it)
                txtEmptyDictionary.visibility = View.INVISIBLE
            }
        })
    }
}