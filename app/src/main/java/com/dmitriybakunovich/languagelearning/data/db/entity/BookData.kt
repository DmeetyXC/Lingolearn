package com.dmitriybakunovich.languagelearning.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "book_data")
data class BookData(@PrimaryKey val bookName: String, val progressRead: Int) : Parcelable