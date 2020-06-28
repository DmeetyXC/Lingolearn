package com.dmitriybakunovich.languagelearning

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmitriybakunovich.languagelearning.book.BookFragment
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.text.TextContainerActivity
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
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.containerMain, BookFragment.newInstance())
                            .commit()
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_dashboard -> {
                        val intent = Intent(this, TextContainerActivity::class.java)
                        // TODO Here the title of the last book should be loaded
                        intent.putExtra("book", BookData("book2", 0, true))
                        startActivity(intent)
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_notifications -> {
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })
    }
}