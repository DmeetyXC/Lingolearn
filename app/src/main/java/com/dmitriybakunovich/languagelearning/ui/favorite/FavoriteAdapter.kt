package com.dmitriybakunovich.languagelearning.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(private val clickListener: OnItemClickListener) :
    ListAdapter<BookData, FavoriteAdapter.FavoriteViewHolder>(DiffCallback()) {

    interface OnItemClickListener {
        fun onItemClick(book: BookData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite,
            parent, false
        )
        val favoriteHolder = FavoriteViewHolder(view)
        view.setOnClickListener {
            clickListener.onItemClick(getItem(favoriteHolder.adapterPosition))
        }
        return favoriteHolder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private class DiffCallback : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.bookName == newItem.bookName
        }

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.isFavourite == newItem.isFavourite
                    && oldItem.isLoad == newItem.isLoad
                    && oldItem.numberPages == newItem.numberPages
                    && oldItem.currentPageRead == newItem.currentPageRead
        }
    }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameBook: TextView = itemView.nameBookFavorite
        private val progressBook: TextView = itemView.progressBookFavorite
        private val imageBook: ImageView = itemView.imageBookFavorite

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