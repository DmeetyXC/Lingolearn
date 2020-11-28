package com.dmitriybakunovich.languagelearning.ui.text

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.databinding.ActivityTextContainerBinding
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf

class TextContainerActivity : AppCompatActivity() {

    private lateinit var viewModel: TextViewModel
    private lateinit var binding: ActivityTextContainerBinding
    private val args: TextContainerActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextContainerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = getViewModel { parametersOf(args.book) }
        initToolbar()
        initView()
        observeView()
    }

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
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
    }*/

    private fun initToolbar() {
        binding.toolbarTextContainer.title = args.book.bookNameTranslate
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
