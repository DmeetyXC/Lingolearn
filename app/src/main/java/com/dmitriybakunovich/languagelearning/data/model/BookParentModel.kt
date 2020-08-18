package com.dmitriybakunovich.languagelearning.data.model

import com.dmitriybakunovich.languagelearning.data.db.entity.BookData

data class BookParentModel(
    val category: String,
    val books: List<BookData>
)