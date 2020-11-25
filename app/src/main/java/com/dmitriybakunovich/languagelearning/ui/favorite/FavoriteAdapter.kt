package com.dmitriybakunovich.languagelearning.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.entity.BookData
import com.dmitriybakunovich.languagelearning.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val clickListener: OnItemClickListener) :
    ListAdapter<BookData, FavoriteAdapter.FavoriteViewHolder>(BookData.DiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(book: BookData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val favoriteHolder = FavoriteViewHolder(binding)
        binding.root.setOnClickListener {
            clickListener.onItemClick(getItem(favoriteHolder.adapterPosition))
        }
        return favoriteHolder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FavoriteViewHolder(binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val nameBook: TextView = binding.nameBookFavorite
        private val progressBook: TextView = binding.progressBookFavorite
        private val imageBook: ImageView = binding.imageBookFavorite

        fun bind(book: BookData) {
            nameBook.text = book.bookName
            setBookProgress(book)
            Glide.with(itemView).load(book.bookCoverPatch).into(imageBook)
        }

        private fun setBookProgress(book: BookData) {
            if (book.isLoad) {
                val progressRead = getProgressReadBook(book)
                if (progressRead == 100) {
                    progressBook.text = itemView.context.getText(R.string.book_full_read)
                } else {
                    progressBook.text = itemView.context.getString(
                        R.string.progress_book,
                        progressRead
                    )
                }
            } else {
                progressBook.text = itemView.context.getString(R.string.book_not_load)
            }
        }

        private fun getProgressReadBook(book: BookData) = with(book) {
            if (!book.isLoad || book.numberPages == 0) {
                return@with 0
            }
            (book.currentPageRead * 100) / book.numberPages
        }
    }
}