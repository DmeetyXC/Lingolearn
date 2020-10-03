package com.dmitriybakunovich.languagelearning.ui.dictionary

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import kotlinx.android.synthetic.main.dictionary_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class DictionaryFragment : Fragment() {

    private val viewModel: DictionaryViewModel by viewModel()
    private lateinit var dictionaryAdapter: DictionaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.dictionary_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observerView()
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_dictionary)
        dictionaryAdapter = DictionaryAdapter()
        val divider = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        with(recyclerDictionary) {
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(divider)
            adapter = dictionaryAdapter
        }
    }

    private fun observerView() {
        viewModel.dictionary.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                txtEmptyDictionary.visibility = View.VISIBLE
            } else {
                dictionaryAdapter.submitList(it)
                txtEmptyDictionary.visibility = View.INVISIBLE
            }
        })
    }
}