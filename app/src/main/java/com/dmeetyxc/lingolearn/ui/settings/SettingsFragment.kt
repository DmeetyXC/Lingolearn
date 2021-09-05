package com.dmeetyxc.lingolearn.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.viewModels
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.manager.PreferenceManagerImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private val viewModel: SettingsViewModel by viewModels()
    private lateinit var prefChild: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        listenerChangeTheme()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
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
            PreferenceManagerImpl.MAIN -> viewModel.handleMainClick()
            PreferenceManagerImpl.CHILD -> viewModel.deleteAllData()
        }
    }

    private fun initView() {
        initialToolbar()
        prefChild = preferenceScreen.findPreference(PreferenceManagerImpl.CHILD)!!
    }

    private fun initialToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbar_settings)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { requireActivity().onBackPressed() }
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
        val listPreference = findPreference(PreferenceManagerImpl.THEME) as ListPreference?
        listPreference.let {
            it?.onPreferenceChangeListener = Preference.OnPreferenceChangeListener { _, newValue ->
                setDefaultNightMode(
                    when (newValue) {
                        PreferenceManagerImpl.THEME_LIGHT -> MODE_NIGHT_NO
                        PreferenceManagerImpl.THEME_DARK -> MODE_NIGHT_YES
                        else -> MODE_NIGHT_FOLLOW_SYSTEM
                    }
                )
                true
            }
        }
    }
}