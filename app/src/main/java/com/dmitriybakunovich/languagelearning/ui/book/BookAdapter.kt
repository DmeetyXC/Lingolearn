package com.dmitriybakunovich.languagelearning.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import kotlinx.android.synthetic.main.item_book.view.*

class BookAdapter(
    private val books: List<BookData>,
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(book: BookData)

        fun onFavoriteItemClick(book: BookData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book, parent, false
        )
        val bookHolder = BookViewHolder(view)
        view.setOnClickListener {
            clickListener.onItemClick(books[bookHolder.adapterPosition])
        }
        bookHolder.favoriteBook.setOnClickListener {
            clickListener.onFavoriteItemClick(books[bookHolder.adapterPosition])
        }
        return bookHolder
    }

    override fun getItemCount(): Int = books.size

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    class BookViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val favoriteBook: ImageView = itemView.favoriteBook
        private val nameBook: TextView = itemView.nameBook
        private val progressBook: TextView = itemView.progressBook

        fun bind(book: BookData) {
            nameBook.text = book.bookName
            setBookProgress(book)
            setBookFavorite(book)
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
}