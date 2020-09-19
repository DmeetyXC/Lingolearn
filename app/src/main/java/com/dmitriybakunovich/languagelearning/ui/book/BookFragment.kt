package com.dmitriybakunovich.languagelearning.ui.book

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import kotlinx.android.synthetic.main.book_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(), BookAdapter.OnItemClickListener {

    private val viewModel: BookViewModel by viewModel()
    private lateinit var parentAdapter: BookParentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.book_fragment, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!viewModel.checkSaveLanguage()) {
            findNavController().navigate(
                BookFragmentDirections.actionBookFragmentToChoiceLanguageFragment()
            )
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observerView()
    }

    private fun initView() {
        swipeRefresh.setOnRefreshListener { viewModel.checkNewBooks() }

        parentAdapter = BookParentAdapter(this)
        with(recyclerBookParent) {
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
            itemAnimator = null
            adapter = parentAdapter
        }
    }

    private fun observerView() {
        viewModel.allBookCategory.observe(viewLifecycleOwner, Observer {
            parentAdapter.submitList(it)
        })

        viewModel.progressState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = it
        })

        viewModel.initBookState.observe(viewLifecycleOwner, Observer {
            findNavController().navigate(
                BookFragmentDirections.actionBookFragmentToTextContainerActivity(it)
            )
        })
    }

    override fun onItemClick(book: BookData) {
        viewModel.handleItemClick(book)
    }

    override fun onFavoriteItemClick(book: BookData) {
        viewModel.addFavoriteBook(book)
    }
}