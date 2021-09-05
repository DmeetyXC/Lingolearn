package com.dmeetyxc.lingolearn.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.data.manager.PreferenceManagerImpl.Companion.BOOK
import com.dmeetyxc.lingolearn.databinding.FragmentFavoriteBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val viewModel: FavoriteViewModel by viewModels()
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFavoriteBinding.bind(view)

        initView()
        observeView()
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_favorite)
        favoriteAdapter = FavoriteAdapter {
            navigateTextContainer(it)
        }
        with(binding.recyclerFavorite) {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = favoriteAdapter
        }
    }

    private fun observeView() {
        viewModel.favoriteBook.observe(viewLifecycleOwner, {
            favoriteAdapter.submitList(it)
            binding.txtEmptyFavorite.isVisible = it.isEmpty()
        })
    }

    private fun navigateTextContainer(book: BookData) {
        val bundle = bundleOf(BOOK to book)
        findNavController().navigate(
            R.id.action_favoriteFragment_to_text_container_activity,
            bundle
        )
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}