package com.dmitriybakunovich.languagelearning.data.db.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookWithText(
    @Embedded val bookData: BookData,
    @Relation(
        parentColumn = "bookName",
        entityColumn = "bookNameText"
    )
    val textData: List<TextData>
)