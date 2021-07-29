package com.dmeetyxc.lingolearn.ui.text

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager.Companion.BOOK
import com.dmeetyxc.lingolearn.databinding.ActivityTextContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TextContainerActivity : AppCompatActivity() {

    private val viewModel: TextViewModel by viewModels()
    private lateinit var binding: ActivityTextContainerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookData: BookData? = intent.getParcelableExtra(BOOK)
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
            binding.txtPageList.isVisible = it
        })
        viewModel.errorState.observe(this, {
            Toast.makeText(this, getString(R.string.error, it), Toast.LENGTH_SHORT).show()
        })
    }
}
