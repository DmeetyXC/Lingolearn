package com.dmeetyxc.lingolearn.ui.book

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dmeetyxc.lingolearn.R
import com.dmeetyxc.lingolearn.data.entity.BookData
import com.dmeetyxc.lingolearn.databinding.ItemBookBinding

class BookAdapter(private val listener: (BookSelectedType, BookData) -> Unit) :
    ListAdapter<BookData, BookAdapter.BookViewHolder>(DiffCallback()) {

    enum class BookSelectedType {
        ITEM,
        FAVORITE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val bookHolder = BookViewHolder(binding)
        binding.root.setOnClickListener {
            listener(BookSelectedType.ITEM, getItem(bookHolder.adapterPosition))
        }
        bookHolder.favoriteBook.setOnClickListener {
            listener(BookSelectedType.FAVORITE_ITEM, getItem(bookHolder.adapterPosition))
        }
        return bookHolder
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookViewHolder(binding: ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {
        val favoriteBook: ImageView = binding.favoriteBook
        private val nameBook: TextView = binding.nameBook
        private val progressBook: TextView = binding.progressBook
        private val imageBook: ImageView = binding.imageBook

        fun bind(book: BookData) {
            nameBook.text = book.bookNameTranslate
            setBookProgress(book)
            setBookFavorite(book)
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

        private fun setBookFavorite(book: BookData) {
            if (book.isFavourite) {
                favoriteBook.setImageResource(R.drawable.ic_baseline_favorite)
            } else {
                favoriteBook.setImageResource(R.drawable.ic_baseline_favorite_not)
            }
        }

        private fun getProgressReadBook(book: BookData) = with(book) {
            if (!book.isLoad || book.numberPages == 0) {
                return@with 0
            }
            (book.currentPageRead * 100) / book.numberPages
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<BookData>() {
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
}