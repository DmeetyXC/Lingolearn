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
            if (it) {
                progressLoadBook.visibility = View.VISIBLE
            } else {
                progressLoadBook.visibility = View.GONE
            }
        })

        viewModel.initBookState.observe(viewLifecycleOwner, Observer {
            navigateTextContainer(it)
        })
    }

    override fun onItemClick(position: Int) {
        viewModel.handleItemClick(adapter.getBook()[position])
    }

    private fun navigateTextContainer(bookData: BookData) {
        val bundle = Bundle()
        bundle.putParcelable("book", bookData)
        findNavController().navigate(R.id.action_bookFragment_to_textContainerActivity, bundle)
    }

    override fun onDestroyView() {
        recyclerBook.adapter = null
        super.onDestroyView()
    }
}