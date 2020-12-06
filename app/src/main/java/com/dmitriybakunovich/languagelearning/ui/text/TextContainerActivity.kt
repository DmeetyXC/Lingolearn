package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.data.manager.PreferenceManager.Companion.BOOK
import com.dmitriybakunovich.languagelearning.databinding.ActivityTextContainerBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TextContainerActivity : AppCompatActivity() {

    private lateinit var viewModel: TextViewModel
    private lateinit var binding: ActivityTextContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookData: BookData? = intent.getParcelableExtra(BOOK)
        viewModel = getViewModel { parametersOf(bookData) }
        initToolbar(bookData)
        initView()
        observeView()
    }

    private fun initToolbar(bookData: BookData?) {
        binding.toolbarTextContainer.title = bookData?.bookNameTranslate
        setSupportActionBar(binding.toolbarTextContainer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarTextContainer.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initView() {
        binding.txtPageList.setOnClickListener {
            viewModel.finishReadBook()
            finish()
        }
        initNavigationButton()
    }

    private fun initNavigationButton() {
        binding.containerNavigationNext.setOnClickListener {
            viewModel.nextPageClick()
        }
        binding.containerNavigationBack.setOnClickListener {
            viewModel.backPageClick()
        }
    }

    private fun observeView() {
        viewModel.lastPageState.observe(this, {
            if (it) {
                binding.txtPageList.visibility = View.VISIBLE
            } else {
                binding.txtPageList.visibility = View.GONE
            }
        })
        viewModel.errorState.observe(this, {
            Toast.makeText(this, getString(R.string.error, it), Toast.LENGTH_SHORT).show()
        })
    }
}
