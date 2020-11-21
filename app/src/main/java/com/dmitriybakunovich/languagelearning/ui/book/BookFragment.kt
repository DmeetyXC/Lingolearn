package com.dmitriybakunovich.languagelearning.ui.book

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.google.android.material.appbar.AppBarLayout
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
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.actionSettings -> findNavController()
                .navigate(BookFragmentDirections.actionBookFragmentToSettingsFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        val appBarLayout = requireActivity().findViewById(R.id.appBarLayout) as AppBarLayout
        appBarLayout.setExpanded(true, true)
        requireActivity().title = getString(R.string.title_library)
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
        viewModel.allBookCategory.observe(viewLifecycleOwner, {
            parentAdapter.submitList(it)
        })

        viewModel.progressState.observe(viewLifecycleOwner, {
            swipeRefresh.isRefreshing = it
        })

        viewModel.initBookState.observe(viewLifecycleOwner, {
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