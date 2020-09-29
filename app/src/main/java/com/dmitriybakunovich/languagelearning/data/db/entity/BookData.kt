package com.dmitriybakunovich.languagelearning.data.db.entity

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "book_data")
data class BookData(
    @PrimaryKey val bookName: String,
    val bookNameTranslate: String,
    val bookCategory: String,
    val currentPageRead: Int,
    val isLoad: Boolean,
    val bookCoverPatch: String?,
    val numberPages: Int = 0,
    val isFavourite: Boolean = false
) : Parcelable {

    class DiffCallback : DiffUtil.ItemCallback<BookData>() {
        override fun areItemsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.bookName == newItem.bookName
        }

        override fun areContentsTheSame(oldItem: BookData, newItem: BookData): Boolean {
            return oldItem.isFavourite == newItem.isFavourite
                    && oldItem.isLoad == newItem.isLoad
                    && oldItem.numberPages == newItem.numberPages
                    && oldItem.currentPageRead == newItem.currentPageRead
        }
    }
}