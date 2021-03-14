package com.dmeetyxc.lingolearn.ui.dictionary

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.FragmentDictionaryBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DictionaryFragment : Fragment(R.layout.fragment_dictionary) {

    private val viewModel: DictionaryViewModel by viewModel()
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