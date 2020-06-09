package com.dmitriybakunovich.languagelearning.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "text_data")
data class TextData(@PrimaryKey val textMain: String, val textChild: String)