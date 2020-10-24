package com.dmitriybakunovich.languagelearning.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.favorite_fragment, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initView()
        observeView()
    }

    override fun onItemClick(book: BookData) {
        findNavController().navigate(
            FavoriteFragmentDirections.actionFavoriteFragmentToTextContainerActivity(book)
        )
    }

    private fun initView() {
        requireActivity().title = getString(R.string.title_favorite)
        favoriteAdapter = FavoriteAdapter(this)
        with(recyclerFavorite) {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = favoriteAdapter
        }
    }

    private fun observeView() {
        viewModel.favoriteBook.observe(viewLifecycleOwner, {
            if (it.isEmpty()) {
                txtEmptyFavorite.visibility = View.VISIBLE
            } else {
                favoriteAdapter.submitList(it)
                txtEmptyFavorite.visibility = View.INVISIBLE
            }
        })
    }
}