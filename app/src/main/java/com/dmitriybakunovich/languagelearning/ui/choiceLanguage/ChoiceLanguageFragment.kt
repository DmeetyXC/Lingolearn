package com.dmitriybakunovich.languagelearning.ui.choiceLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmitriybakunovich.languagelearning.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.choice_language_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ChoiceLanguageFragment : Fragment() {

    private val viewModel: ChoiceLanguageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.choice_language_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observeView()
        viewModel.setInitialPositionLang()
    }

    private fun initView() {
        requireActivity().title = getString(R.string.app_name)
        changeVisibleNavigation(false)
        next.setOnClickListener { nextClick() }
    }

    private fun observeView() {
        viewModel.childSelectState.observe(viewLifecycleOwner, {
            spinnerChild.setSelection(it)
        })
    }

    private fun nextClick() {
        val mainLanguage = viewModel.getSelectValues(spinnerMain.selectedItemPosition)
        val childLanguage = viewModel.getSelectValues(spinnerChild.selectedItemPosition)
        if (mainLanguage == childLanguage) {
            Snackbar.make(requireView(), R.string.different_choice_lang, Snackbar.LENGTH_SHORT)
                .show()
        } else {
            viewModel.saveLanguageChoice(mainLanguage, childLanguage)
            changeVisibleNavigation(true)
            findNavController().navigate(
                ChoiceLanguageFragmentDirections.actionChoiceLanguageFragmentToBookFragment()
            )
        }
    }

    // TODO Redesign navigation structure, this remove
    private fun changeVisibleNavigation(visibility: Boolean) {
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
        if (visibility) navView.visibility = View.VISIBLE
        else navView.visibility = View.GONE
    }
}