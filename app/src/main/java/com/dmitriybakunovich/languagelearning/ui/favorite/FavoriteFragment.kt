package com.dmitriybakunovich.languagelearning.ui.favorite

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.data.manager.PreferenceManager.Companion.BOOK
import com.dmitriybakunovich.languagelearning.databinding.FavoriteFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(R.layout.favorite_fragment), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModel()
    private var _binding: FavoriteFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FavoriteFragmentBinding.bind(view)

        initView()
        observeView()
    }

    override fun onItemClick(book: BookData) {
        val bundle = Bundle()
        bundle.putParcelable(BOOK, book)
        findNavController().navigate(R.id.action_favoriteFragment_to_textContainerActivity, bundle)
    }

    private fun initView() {
        favoriteAdapter = FavoriteAdapter(this)
        with(binding.recyclerFavorite) {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = favoriteAdapter
        }
    }

    private fun observeView() {
        viewModel.favoriteBook.observe(viewLifecycleOwner, {
            favoriteAdapter.submitList(it)
            if (it.isEmpty()) {
                binding.txtEmptyFavorite.visibility = View.VISIBLE
            } else {
                binding.txtEmptyFavorite.visibility = View.INVISIBLE
            }
        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}