package com.dmitriybakunovich.languagelearning.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolbar()
        initNavigation()
    }

    fun changeVisibleNavigation(visibility: Boolean) {
        if (visibility) binding.bottomNav.visibility = View.VISIBLE
        else binding.bottomNav.visibility = View.GONE
    }

    fun expandedAppBar() {
        binding.appBarMain.setExpanded(true, true)
    }

    fun changeScrollingToolbar(isScroll: Boolean) {
        val params = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
        if (isScroll) params.scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
        else params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
    }

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        initNavigationClickListener()
    }

    private fun initNavigationClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}