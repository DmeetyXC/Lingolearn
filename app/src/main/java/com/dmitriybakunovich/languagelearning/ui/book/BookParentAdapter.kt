package com.dmitriybakunovich.languagelearning.ui.book

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.model.BookParentModel
import kotlinx.android.synthetic.main.item_book_category.view.*

class BookParentAdapter(
    private val categoryBook: List<BookParentModel>,
    private val clickListener: BookAdapter.OnItemClickListener
) :
    RecyclerView.Adapter<BookParentAdapter.BookParentViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book_category, parent, false
        )
        return BookParentViewHolder(view, viewPool)
    }

    override fun getItemCount(): Int = categoryBook.size

    override fun onBindViewHolder(holder: BookParentViewHolder, position: Int) {
        holder.bind(categoryBook[position], clickListener)
    }

    class BookParentViewHolder(
        itemView: View,
        private val viewPool: RecyclerView.RecycledViewPool
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val recyclerView: RecyclerView = itemView.recyclerBookChild
        private val textCategory: TextView = itemView.textTitle

        fun bind(book: BookParentModel, clickListener: BookAdapter.OnItemClickListener) {
            textCategory.text = book.category
            val childLayoutManager = LinearLayoutManager(
                recyclerView.context, LinearLayoutManager.HORIZONTAL, false
            )
            childLayoutManager.initialPrefetchItemCount = 3
            recyclerView.apply {
                layoutManager = childLayoutManager
                adapter = BookAdapter(book.books, clickListener)
                setRecycledViewPool(viewPool)
            }
        }
    }
}