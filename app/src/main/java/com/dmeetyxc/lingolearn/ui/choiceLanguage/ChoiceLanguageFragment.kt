package com.dmeetyxc.lingolearn.ui.choiceLanguage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.ChoiceLanguageFragmentBinding
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
    }

    private fun initView() {
        binding.next.setOnClickListener { nextClick() }
    }

    private fun observeView() {
        viewModel.childSelectState.observe(viewLifecycleOwner, {
            binding.spinnerChild.setSelection(it)
        })
        viewModel.languageState.observe(viewLifecycleOwner, {
            if (!it)
                findNavController().navigate(R.id.action_choice_language_fragment_to_containerFragment)
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
            findNavController().navigate(R.id.action_choice_language_fragment_to_containerFragment)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}