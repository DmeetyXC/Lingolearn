package com.dmitriybakunovich.languagelearning.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModel()
    private lateinit var prefChild: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        listenerChangeTheme()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefChild = preferenceScreen.findPreference("child")!!
        initToolbar()
        observeView()
        viewModel.initChildPref()
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        when (key) {
            "main" -> {
                viewModel.handleMainClick()
            }
            "child" -> {
                viewModel.deleteAllData()
            }
        }
    }

    private fun initToolbar() {
        requireActivity().title = getString(R.string.settings)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as MainActivity).changeScrollingToolbar(false)
        (activity as MainActivity).changeVisibleNavigation(false)
    }

    private fun observeView() {
        viewModel.childPreferenceSate.observe(viewLifecycleOwner, {
            prefChild.entries = it.first
            prefChild.entryValues = it.second
        })
        viewModel.childSelectItemSate.observe(viewLifecycleOwner, {
            val indexLanguage = prefChild.findIndexOfValue(it)
            if (indexLanguage != -1) {
                prefChild.setValueIndex(indexLanguage)
            } else {
                prefChild.setValueIndex(0)
            }
        })
    }

    private fun listenerChangeTheme() {
        val listPreference = findPreference("theme") as ListPreference?
        listPreference.let {
            it?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                setDefaultNightMode(
                    when (newValue) {
                        "light" -> MODE_NIGHT_NO
                        "dark" -> MODE_NIGHT_YES
                        else -> MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
                true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as MainActivity).changeVisibleNavigation(true)
        (activity as MainActivity).changeScrollingToolbar(true)
    }
}