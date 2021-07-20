package com.dmeetyxc.lingolearn.ui.choiceLanguage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.FragmentChoiceLanguageBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChoiceLanguageFragment : Fragment(R.layout.fragment_choice_language) {

    private val viewModel: ChoiceLanguageViewModel by viewModels()
    private var _binding: FragmentChoiceLanguageBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentChoiceLanguageBinding.bind(view)

        initView()
        observeView()
    }

    private fun initView() {
        binding.next.setOnClickListener { nextClick() }
    }

    private fun observeView() {
        viewModel.childSelectState.observe(viewLifecycleOwner, {
            binding.spinnerChild.setSelection(it)
        })
    }

    private fun nextClick() {
        val mainLanguage = viewModel.getSelectValues(binding.spinnerMain.selectedItemPosition)
        val childLanguage = viewModel.getSelectValues(binding.spinnerChild.selectedItemPosition)
        if (mainLanguage == childLanguage) {
            Snackbar.make(requireView(), R.string.different_choice_lang, Snackbar.LENGTH_SHORT)
                .show()
        } else {
            viewModel.saveLanguageChoice(mainLanguage, childLanguage)
            findNavController().navigate(R.id.action_choice_language_fragment_to_mainFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}