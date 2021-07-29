package com.dmeetyxc.lingolearn.ui.dictionary

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.FragmentDictionaryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {

    private val viewModel: DictionaryViewModel by viewModels()
    private var _binding: FragmentDictionaryBinding? = null
    private val binding get() = _binding!!
    private lateinit var dictionaryAdapter: DictionaryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDictionaryBinding.bind(view)

        initView()
        observeView()
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_dictionary)
        dictionaryAdapter = DictionaryAdapter()
        val divider = DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL)
        with(binding.recyclerDictionary) {
            layoutManager = LinearLayoutManager(requireActivity())
            addItemDecoration(divider)
            adapter = dictionaryAdapter
        }
    }

    private fun observeView() {
        viewModel.dictionary.observe(viewLifecycleOwner, {
            dictionaryAdapter.submitList(it)
            binding.txtEmptyDictionary.isVisible = it.isEmpty()
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}