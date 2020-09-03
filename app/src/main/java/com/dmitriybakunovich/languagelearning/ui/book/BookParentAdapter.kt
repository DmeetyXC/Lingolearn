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

class BookParentAdapter(private val clickListener: BookAdapter.OnItemClickListener) :
    RecyclerView.Adapter<BookParentAdapter.BookParentViewHolder>() {

    private val viewPool = RecyclerView.RecycledViewPool()
    private var categoryBook: List<BookParentModel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookParentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_book_category, parent, false
        )
        val adapter = BookAdapter(clickListener)
        val viewHolder = BookParentViewHolder(view, adapter)
        initRecycler(viewHolder, adapter)
        return viewHolder
    }

    private fun initRecycler(viewHolder: BookParentViewHolder, bookAdapter: BookAdapter) {
        viewHolder.recyclerView.apply {
            layoutManager = LinearLayoutManager(
                viewHolder.recyclerView.context, LinearLayoutManager.HORIZONTAL, false
            )
            setRecycledViewPool(viewPool)
            adapter = bookAdapter
        }
    }

    override fun getItemCount(): Int = categoryBook.size

    override fun onBindViewHolder(holder: BookParentViewHolder, position: Int) {
        holder.bind(categoryBook[position])
    }

    fun setItems(categoryBook: List<BookParentModel>) {
        this.categoryBook = categoryBook
        notifyDataSetChanged()
    }

    class BookParentViewHolder(itemView: View, private var adapter: BookAdapter) :
        RecyclerView.ViewHolder(itemView) {

        val recyclerView: RecyclerView = itemView.recyclerBookChild
        private val textCategory: TextView = itemView.textTitle

        fun bind(book: BookParentModel) {
            textCategory.text = book.category
            adapter.setItems(book.books)
        }
    }
}