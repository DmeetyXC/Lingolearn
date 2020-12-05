package com.dmitriybakunovich.languagelearning.ui.choiceLanguage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.databinding.ChoiceLanguageFragmentBinding
import com.dmitriybakunovich.languagelearning.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoiceLanguageFragment : Fragment(R.layout.choice_language_fragment) {

    private val viewModel: ChoiceLanguageViewModel by viewModel()
    private var _binding: ChoiceLanguageFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ChoiceLanguageFragmentBinding.bind(view)

        initView()
        observeView()
        viewModel.setInitialPositionLang()
    }

    private fun initView() {
        requireActivity().title = getString(R.string.app_name)
        (activity as MainActivity).changeVisibleNavigation(false)
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
            (activity as MainActivity).changeVisibleNavigation(true)
            findNavController().navigate(R.id.action_choiceLanguageFragment_to_bookFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}