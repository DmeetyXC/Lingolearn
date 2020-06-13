package com.dmitriybakunovich.languagelearning.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "book_data")
data class BookData(@PrimaryKey val bookName: String, val progressRead: Int)