package com.dmeetyxc.lingolearn.ui.favorite

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
import com.dmeetyxc.lingolearn.databinding.ItemFavoriteBinding

class FavoriteAdapter(private val listener: (BookData) -> Unit) :
    ListAdapter<BookData, FavoriteAdapter.FavoriteViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = ItemFavoriteBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        val favoriteHolder = FavoriteViewHolder(binding)
        binding.root.setOnClickListener {
            listener(getItem(favoriteHolder.adapterPosition))
        }
        return favoriteHolder
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: FavoriteViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNullOrEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            holder.bind(payloads)
        }
    }

    class FavoriteViewHolder(binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
        private val nameBook: TextView = binding.nameBookFavorite
        private val progressBook: TextView = binding.progressBookFavorite
        private val imageBook: ImageView = binding.imageBookFavorite

        fun bind(book: BookData) {
            nameBook.text = book.bookNameTranslate
            setBookProgress(book)
            Glide.with(itemView).load(book.bookCoverPatch).into(imageBook)
        }

        fun bind(payloads: List<Any>) {
            val book = payloads.last() as BookData
            setBookProgress(book)
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

    private object DiffCallback : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.bookName == newItem.bookName
        }

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.isFavourite == newItem.isFavourite
                    && oldItem.isLoad == newItem.isLoad
                    && oldItem.numberPages == newItem.numberPages
                    && oldItem.currentPageRead == newItem.currentPageRead
        }

        override fun getChangePayload(oldItem: BookData, newItem: BookData): Any? {
            if (oldItem.isFavourite != newItem.isFavourite
                || oldItem.currentPageRead != newItem.currentPageRead
            ) return newItem
            return super.getChangePayload(oldItem, newItem)
        }
    }
}