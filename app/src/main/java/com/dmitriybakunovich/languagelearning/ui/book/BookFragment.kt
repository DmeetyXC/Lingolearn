package com.dmitriybakunovich.languagelearning.ui.book

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.databinding.BookFragmentBinding
import com.dmitriybakunovich.languagelearning.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(R.layout.book_fragment), BookAdapter.OnItemClickListener {

    private val viewModel: BookViewModel by viewModel()
    private var _binding: BookFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var parentAdapter: BookParentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!viewModel.checkSaveLanguage()) {
            findNavController().navigate(
                BookFragmentDirections.actionBookFragmentToChoiceLanguageFragment()
            )
        }
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = BookFragmentBinding.bind(view)

        initView()
        observerView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        requireActivity().menuInflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController()
                .navigate(BookFragmentDirections.actionBookFragmentToSettingsFragment())
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(book: BookData) {
        viewModel.handleItemClick(book)
    }

    override fun onFavoriteItemClick(book: BookData) {
        viewModel.addFavoriteBook(book)
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_library)
        (activity as MainActivity).expandedAppBar()
        binding.swipeRefresh.setOnRefreshListener { viewModel.checkNewBooks() }

        parentAdapter = BookParentAdapter(this)
        with(binding.recyclerBookParent) {
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
            binding.swipeRefresh.isRefreshing = it
        })

        viewModel.initBookState.observe(viewLifecycleOwner, {
            findNavController().navigate(
                BookFragmentDirections.actionBookFragmentToTextContainerActivity(it)
            )
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}