package com.dmitriybakunovich.languagelearning.data.entity

import androidx.recyclerview.widget.DiffUtil

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