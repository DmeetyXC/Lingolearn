package com.dmeetyxc.lingolearn.ui.book

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager.Companion.BOOK
import com.dmeetyxc.lingolearn.databinding.FragmentBookBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookFragment : Fragment(R.layout.fragment_book) {

    private val viewModel: BookViewModel by viewModels()
    private var _binding: FragmentBookBinding? = null
    private val binding get() = _binding!!
    private lateinit var parentAdapter: BookParentAdapter

    companion object {
        private const val SHORTCUT_FAVORITE = "com.dmeetyxc.lingolearn.shortcut.FAVORITE"
        private const val SHORTCUT_DICTIONARY = "com.dmeetyxc.lingolearn.shortcut.DICTIONARY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleShortcut(requireActivity().intent.action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentBookBinding.bind(view)

        initView()
        observeView()
    }

    private fun handleShortcut(intentAction: String?) {
        val navController = findNavController()
        if (navController.currentDestination?.id != R.id.favorite_fragment
            && navController.currentDestination?.id != R.id.dictionary_fragment
        ) {
            when (intentAction) {
                SHORTCUT_FAVORITE -> {
                    navController.navigate(R.id.action_book_fragment_to_favorite_fragment)
                }
                SHORTCUT_DICTIONARY -> {
                    navController.navigate(R.id.action_book_fragment_to_dictionary_fragment)
                }
            }
            if (intentAction != Intent.ACTION_MAIN) requireActivity().intent.action = ""
        }
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_library)
        binding.swipeRefresh.setOnRefreshListener { viewModel.checkNewBooks() }

        parentAdapter = BookParentAdapter { bookSelectedType, book ->
            when (bookSelectedType) {
                BookAdapter.BookSelectedType.ITEM -> viewModel.handleItemClick(book)
                BookAdapter.BookSelectedType.FAVORITE_ITEM -> viewModel.addFavoriteBook(book)
            }
        }

        with(binding.recyclerBookParent) {
            layoutManager = LinearLayoutManager(
                requireActivity(), LinearLayoutManager.VERTICAL, false
            )
            itemAnimator = null
            adapter = parentAdapter
        }
    }

    private fun observeView() {
        viewModel.booksCategoryState().observe(viewLifecycleOwner, {
            parentAdapter.submitList(it)
            changeVisibleTextEmpty(it.isEmpty())
        })

        viewModel.progressState.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = it
        })

        viewModel.languageState.observe(viewLifecycleOwner, {
            if (!it) navigateChoiceLanguage()
        })

        viewModel.initBookState.observe(viewLifecycleOwner, {
            navigateTextContainer(it)
        })

        viewModel.networkState().observe(viewLifecycleOwner, {
            if (it) {
                changeVisibleTextEmpty(false)
                viewModel.checkNewBooks()
            }
        })
    }

    private fun changeVisibleTextEmpty(stateVisibility: Boolean) {
        if (stateVisibility) binding.txtEmptyBook.visibility = View.VISIBLE
        else binding.txtEmptyBook.visibility = View.INVISIBLE
    }

    private fun navigateChoiceLanguage() {
        val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
        navController.navigate(R.id.choice_language_fragment)
    }

    private fun navigateTextContainer(bookData: BookData) {
        val bundle = bundleOf(BOOK to bookData)
        findNavController().navigate(R.id.action_bookFragment_to_text_container_activity, bundle)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}