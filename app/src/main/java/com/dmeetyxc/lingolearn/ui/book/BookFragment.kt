package com.dmeetyxc.lingolearn.ui.book

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManager.Companion.BOOK
import com.dmeetyxc.lingolearn.databinding.BookFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookFragment : Fragment(R.layout.book_fragment), BookAdapter.OnItemClickListener {

    private val viewModel: BookViewModel by viewModel()
    private var _binding: BookFragmentBinding? = null
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
        _binding = BookFragmentBinding.bind(view)

        initView()
        observeView()
    }

    override fun onItemClick(book: BookData) {
        viewModel.handleItemClick(book)
    }

    override fun onFavoriteItemClick(book: BookData) {
        viewModel.addFavoriteBook(book)
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
        }
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_library)
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

    private fun observeView() {
        viewModel.allBookCategory.observe(viewLifecycleOwner, {
            parentAdapter.submitList(it)
        })

        viewModel.progressState.observe(viewLifecycleOwner, {
            binding.swipeRefresh.isRefreshing = it
        })

        viewModel.initBookState.observe(viewLifecycleOwner, {
            navigateTextContainer(it)
        })
    }

    private fun navigateTextContainer(bookData: BookData) {
        val bundle = Bundle()
        bundle.putParcelable(BOOK, bookData)
        findNavController().navigate(R.id.action_bookFragment_to_text_container_activity, bundle)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}