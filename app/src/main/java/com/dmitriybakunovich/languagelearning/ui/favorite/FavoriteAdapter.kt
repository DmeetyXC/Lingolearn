package com.dmitriybakunovich.languagelearning.ui.favorite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(
    private val favoriteBooks: List<BookData>,
    private val clickListener: OnItemClickListener
) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_favorite,
            parent, false
        )
        val favoriteHolder = FavoriteViewHolder(view)
        view.setOnClickListener {
            clickListener.onItemClick(favoriteHolder.adapterPosition)
        }
        return favoriteHolder
    }

    override fun getItemCount(): Int = favoriteBooks.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(favoriteBooks[position])
    }

    fun getBook(): List<BookData> = favoriteBooks

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameBookFavorite: TextView = itemView.nameBookFavorite
        private val progressBookFavorite: TextView = itemView.progressBookFavorite

        fun bind(book: BookData) {
            nameBookFavorite.text = book.bookName
            setBookProgress(book)
        }

        private fun setBookProgress(book: BookData) {
            if (book.isLoad) {
                val progressRead = getProgressReadBook(book)
                if (progressRead == 100) {
                    progressBookFavorite.text = itemView.context.getText(R.string.book_full_read)
                } else {
                    progressBookFavorite.text = itemView.context.getString(
                        R.string.progress_book,
                        progressRead
                    )
                }
            } else {
                progressBookFavorite.text = itemView.context.getString(R.string.book_not_load)
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