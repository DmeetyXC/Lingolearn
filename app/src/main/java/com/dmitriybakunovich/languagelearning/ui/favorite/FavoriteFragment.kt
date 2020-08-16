package com.dmitriybakunovich.languagelearning.ui.favorite

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
import kotlinx.android.synthetic.main.favorite_fragment.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : Fragment(), FavoriteAdapter.OnItemClickListener {

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recyclerFavorite.layoutManager = LinearLayoutManager(requireActivity())
        observerView()
    }

    private fun observerView() {
        viewModel.favoriteBook.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) {
                txtEmptyFavorite.visibility = View.VISIBLE
            } else {
                adapter = FavoriteAdapter(it, this)
                recyclerFavorite.adapter = adapter
                txtEmptyFavorite.visibility = View.INVISIBLE
            }
        })
    }

    private fun navigateTextContainer(bookData: BookData) {
        val bundle = Bundle()
        bundle.putParcelable("book", bookData)
        findNavController().navigate(R.id.action_favoriteFragment_to_textContainerActivity, bundle)
    }

    override fun onDestroyView() {
        recyclerFavorite.adapter = null
        super.onDestroyView()
    }

    override fun onItemClick(position: Int) {
        navigateTextContainer(adapter.getBook()[position])
    }
}