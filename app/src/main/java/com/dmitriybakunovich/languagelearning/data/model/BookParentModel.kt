package com.dmitriybakunovich.languagelearning.data.model

import androidx.recyclerview.widget.DiffUtil
import com.dmitriybakunovich.languagelearning.data.db.entity.BookData

data class BookParentModel(
    val category: String,
    val books: List<BookData>
) {
    class DiffCallback : DiffUtil.ItemCallback<BookParentModel>() {
        override fun areItemsTheSame(oldItem: BookParentModel, newItem: BookParentModel): Boolean {
            return oldItem.category == newItem.category
        }

        override fun areContentsTheSame(
            oldItem: BookParentModel,
            newItem: BookParentModel
        ): Boolean {
            return oldItem.books == newItem.books
        }
    }
}