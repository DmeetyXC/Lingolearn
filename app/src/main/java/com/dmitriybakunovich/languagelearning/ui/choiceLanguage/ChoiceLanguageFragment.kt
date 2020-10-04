package com.dmitriybakunovich.languagelearning.ui.choiceLanguage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dmitriybakunovich.languagelearning.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.choice_language_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class ChoiceLanguageFragment : Fragment() {

    private val viewModel: ChoiceLanguageViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.choice_language_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        val navView: BottomNavigationView = requireActivity().findViewById(R.id.bottom_nav)
        navView.visibility = View.GONE

        // TODO remove Language from the list of native languages if you want to learn it
        val selectedValues = resources.getStringArray(R.array.languages_value)
        val positionSystemLanguage =
            selectedValues.indexOfFirst { it == Locale.getDefault().language }
        spinnerChild.setSelection(positionSystemLanguage)

        next.setOnClickListener {
            val mainLanguage = selectedValues[spinnerMain.selectedItemPosition]
            val childLanguage = selectedValues[spinnerChild.selectedItemPosition]
            viewModel.saveLanguageChoice(mainLanguage, childLanguage)
            navView.visibility = View.VISIBLE
            findNavController().navigate(
                ChoiceLanguageFragmentDirections.actionChoiceLanguageFragmentToBookFragment()
            )
        }
    }

    private fun initView() {
        requireActivity().title = getString(R.string.app_name)
    }
}