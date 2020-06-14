package com.dmitriybakunovich.languagelearning.text

import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import kotlinx.android.synthetic.main.activity_text_container.*

class TextContainerActivity : AppCompatActivity() {
    private lateinit var viewModel: TextViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_container)

        val bookName = intent.getParcelableExtra<BookData>("book")
        viewModel = ViewModelProvider(this, TextViewModelFactory(this.application, bookName))
            .get(TextViewModel::class.java)
        initToolbar()
        startText()
        observeView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.text_menu, menu)
        val textNext = menu?.findItem(R.id.textNext)
        val textBack = menu?.findItem(R.id.textBack)
        textNext?.setOnMenuItemClickListener {
            Log.v("QQQQ", "next button")
            return@setOnMenuItemClickListener true
        }
        textBack?.setOnMenuItemClickListener {
            Log.v("QQQQ", "back button")
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
