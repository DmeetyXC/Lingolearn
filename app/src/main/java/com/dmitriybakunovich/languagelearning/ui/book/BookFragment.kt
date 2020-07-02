package com.dmitriybakunovich.languagelearning.ui.book

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import com.dmitriybakunovich.languagelearning.ui.text.TextContainerActivity
import kotlinx.android.synthetic.main.book_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(), BookAdapter.OnItemClickListener {

    companion object {
        fun newInstance() = BookFragment()
    }

    private val viewModel: BookViewModel by viewModel()
    private lateinit var adapter: BookAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.book_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

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