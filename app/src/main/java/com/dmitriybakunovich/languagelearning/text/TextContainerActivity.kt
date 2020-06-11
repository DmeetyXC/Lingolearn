package com.dmitriybakunovich.languagelearning.text

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dmitriybakunovich.languagelearning.R
import kotlinx.android.synthetic.main.activity_text_container.*

class TextContainerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_container)

        initToolbar()
        startText()
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarTextContainer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTextContainer.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun startText() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TextMainFragment.newInstance())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(
                R.id.container2,
                TextChildFragment.newInstance()
            )
            .commit()
    }
}
