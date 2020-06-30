package com.dmitriybakunovich.languagelearning.book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.text.TextContainerActivity
import kotlinx.android.synthetic.main.book_fragment.*

class BookFragment : Fragment(), BookAdapter.OnItemClickListener {
    private lateinit var adapter: BookAdapter

    companion object {
        fun newInstance() = BookFragment()
    }

    private lateinit var viewModel: BookViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.book_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BookViewModel::class.java)

        initView()
        observerView()
    }

    private fun initView() {
        recyclerBook.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun observerView() {
        viewModel.allBook.observe(viewLifecycleOwner, Observer {
            adapter = BookAdapter(it, this)
            recyclerBook.adapter = adapter
        })

        viewModel.progressState.observe(viewLifecycleOwner, Observer {
            if (it.first) {
                progressLoadBook.visibility = View.VISIBLE
            } else {
                progressLoadBook.visibility = View.GONE
                startTextContainerActivity(it.second)
            }
        })
    }

    override fun onItemClick(position: Int) {
        val book = adapter.getBook()[position]
        if (!book.isLoad) {
            viewModel.initBook(book)
        } else {
            startTextContainerActivity(book)
        }
    }

    private fun startTextContainerActivity(bookData: BookData) {
        val intent = Intent(requireActivity(), TextContainerActivity::class.java)
        intent.putExtra("book", bookData)
        startActivity(intent)
    }
}