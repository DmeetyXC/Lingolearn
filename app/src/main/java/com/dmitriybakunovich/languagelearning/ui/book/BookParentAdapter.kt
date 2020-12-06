package com.dmitriybakunovich.languagelearning.ui.book

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.data.entity.BookParentModel
import com.dmitriybakunovich.languagelearning.databinding.ItemBookCategoryBinding

class BookParentAdapter(private val clickListener: BookAdapter.OnItemClickListener) :
    ListAdapter<BookParentModel, BookParentAdapter.BookParentViewHolder>(
        BookParentModel.DiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookParentViewHolder {
        val binding = ItemBookCategoryBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val adapter = BookAdapter(clickListener)
        val viewHolder = BookParentViewHolder(binding, adapter)
        initRecycler(viewHolder, adapter)
        return viewHolder
    }

    private fun initRecycler(viewHolder: BookParentViewHolder, bookAdapter: BookAdapter) {
        with(viewHolder.recyclerView) {
            layoutManager = LinearLayoutManager(
                viewHolder.recyclerView.context, LinearLayoutManager.HORIZONTAL, false
            )
            itemAnimator = null
            adapter = bookAdapter
        }
    }

    override fun onBindViewHolder(holder: BookParentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookParentViewHolder(binding: ItemBookCategoryBinding, private var adapter: BookAdapter) :
        RecyclerView.ViewHolder(binding.root) {

        val recyclerView: RecyclerView = binding.recyclerBookChild
        private val textCategory: TextView = binding.textTitle

        fun bind(book: BookParentModel) {
            textCategory.text = book.category
            adapter.submitList(book.books)
        }
    }
}