package com.dmitriybakunovich.languagelearning.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initToolbar()
        initNavigation()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
    }

    private fun initNavigation() {
        val navController = Navigation
            .findNavController(this, R.id.nav_host_fragment)
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav)
        // Will automatically go to menu items, ItemSelectedListener not needed
        // NavigationUI.setupWithNavController(navView, navController)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bookFragment -> {
                    navController.navigate(R.id.bookFragment)
                }
                R.id.textContainerActivity -> {
                    // TODO Here the title of the last book should be loaded
                    val bundle = Bundle()
                    bundle.putParcelable("book", BookData("book2", 0, true))
                    navController.navigate(
                        R.id.action_bookFragment_to_textContainerActivity,
                        bundle
                    )
                }
                R.id.dictionaryFragment -> {
                }
            }
            false
        }
    }
}