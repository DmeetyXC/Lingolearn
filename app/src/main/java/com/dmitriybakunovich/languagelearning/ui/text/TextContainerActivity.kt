package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.dmitriybakunovich.languagelearning.R
import kotlinx.android.synthetic.main.activity_text_container.*
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TextContainerActivity : AppCompatActivity() {
    private lateinit var viewModel: TextViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_container)

        viewModel = getViewModel { parametersOf(intent.getParcelableExtra("book")) }
        initToolbar()
        startText()
        observeView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.text_menu, menu)
        val textNext = menu?.findItem(R.id.textNext)
        val textBack = menu?.findItem(R.id.textBack)
        textNext?.setOnMenuItemClickListener {
            viewModel.nextPageClick()
            return@setOnMenuItemClickListener true
        }
        textBack?.setOnMenuItemClickListener {
            viewModel.backPageClick()
            return@setOnMenuItemClickListener true
        }
        return true
    }

    private fun initToolbar() {
        setSupportActionBar(toolbarTextContainer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbarTextContainer.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun observeView() {
        /*viewModel.allText.observe(this, Observer {
            if (it.isNotEmpty()) {
                Log.v("QQQQ", "book in activity = ${it[0].bookData.bookName}")
            }
        })
        viewModel.book.observe(this, Observer {
            Log.v("QQQQQQQQQQ", "WATCH = ${it[0].textMain}")
        })*/
    }

    private fun startText() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, TextMainFragment.newInstance())
            .commit()

        supportFragmentManager.beginTransaction()
            .replace(R.id.container2, TextChildFragment.newInstance())
            .commit()
    }
}
