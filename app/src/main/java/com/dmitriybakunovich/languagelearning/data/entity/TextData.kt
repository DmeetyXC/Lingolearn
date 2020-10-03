package com.dmitriybakunovich.languagelearning.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_data")
data class TextData(
    val bookNameText: String,
    val textMain: String,
    val textChild: String
) {
    @PrimaryKey(autoGenerate = true)
    var idTextData: Long = 0
}