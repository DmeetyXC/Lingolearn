package com.dmitriybakunovich.languagelearning.ui.dictionary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.databinding.DictionaryFragmentBinding
import com.google.android.material.appbar.AppBarLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class DictionaryFragment : Fragment(R.layout.dictionary_fragment) {

    private val viewModel: DictionaryViewModel by viewModel()
    private var _binding: DictionaryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var dictionaryAdapter: DictionaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = DictionaryFragmentBinding.bind(view)

        initView()
        observerView()
    }

    private fun initView() {
        val appBarLayout = requireActivity().findViewById(R.id.app_bar_main) as AppBarLayout
        appBarLayout.setExpanded(true, true)
        requireActivity().title = getString(R.string.title_dictionary)
        dictionaryAdapter = DictionaryAdapter()
        val divider = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        with(binding.recyclerDictionary) {
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(divider)
            adapter = dictionaryAdapter
        }
    }

    private fun observerView() {
        viewModel.dictionary.observe(viewLifecycleOwner, {
            dictionaryAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.txtEmptyDictionary.visibility = View.VISIBLE
            } else {
                binding.txtEmptyDictionary.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}