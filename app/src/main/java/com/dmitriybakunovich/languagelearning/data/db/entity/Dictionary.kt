package com.dmitriybakunovich.languagelearning.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Dictionary(
    val dictionaryValue: String,
    val dictionaryTranslateValue: String
) {
    @PrimaryKey(autoGenerate = true)
    var idDictionary: Long = 0
}