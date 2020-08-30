package com.dmitriybakunovich.languagelearning.data.db.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "book_data")
data class BookData(
    @PrimaryKey val bookName: String,
    val bookCategory: String,
    val currentPageRead: Int,
    val isLoad: Boolean,
    val bookCoverPatch: String?,
    val numberPages: Int = 0,
    val isFavourite: Boolean = false
) :
    Parcelable