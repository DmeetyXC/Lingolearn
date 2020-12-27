package com.dmeetyxc.lingolearn.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
import com.google.android.material.appbar.AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initNavigation()
    }

    private fun initNavigation() {
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.bottomNav.setupWithNavController(navController)
        initAppBar(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destinationChanged(destination.id)
        }
    }

    private fun initAppBar(navController: NavController) {
        setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.choice_language_fragment,
                R.id.book_fragment,
                R.id.favorite_fragment,
                R.id.dictionary_fragment
            )
        )
        findViewById<Toolbar>(R.id.toolbar)
            .setupWithNavController(navController, appBarConfiguration)
    }

    private fun destinationChanged(destinationId: Int) {
        binding.appBarMain.setExpanded(true, true)
        when (destinationId) {
            R.id.choice_language_fragment -> {
                changeVisibleNavigation(false)
            }
            R.id.book_fragment -> {
                changeScrollingToolbar(true)
                changeVisibleNavigation(true)
            }
            R.id.settings_fragment -> {
                changeScrollingToolbar(false)
                changeVisibleNavigation(false)
            }
        }
    }

    private fun changeVisibleNavigation(visibility: Boolean) {
        if (visibility) binding.bottomNav.visibility = View.VISIBLE
        else binding.bottomNav.visibility = View.GONE
    }

    private fun changeScrollingToolbar(isScroll: Boolean) {
        val params = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
        if (isScroll) params.scrollFlags = SCROLL_FLAG_SCROLL or SCROLL_FLAG_ENTER_ALWAYS
        else params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
    }
}