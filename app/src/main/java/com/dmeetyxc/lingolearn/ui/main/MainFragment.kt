package com.dmeetyxc.lingolearn.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.databinding.FragmentMainBinding
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMainBinding.bind(view)

        initNavigation()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return item.onNavDestinationSelected(findNavController())
                || super.onOptionsItemSelected(item)
    }

    private fun initNavigation() {
        val navHostFragment = childFragmentManager
            .findFragmentById(R.id.nav_host_fragment_base) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNav.setupWithNavController(navController)
        initAppBar(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            destinationChanged(destination.id)
        }
    }

    private fun initAppBar(navController: NavController) {
        (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.book_fragment,
                R.id.favorite_fragment,
                R.id.dictionary_fragment
            )
        )
        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
    }

    private fun destinationChanged(destinationId: Int) {
        binding.appBarMain.setExpanded(true, true)
        when (destinationId) {
            R.id.book_fragment -> changeScrollingToolbar(true)
            R.id.settings_fragment -> changeScrollingToolbar(false)
        }
    }

    private fun changeScrollingToolbar(isScroll: Boolean) {
        val params = binding.toolbar.layoutParams as AppBarLayout.LayoutParams
        if (isScroll) params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or
                AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
        else params.scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_NO_SCROLL
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}