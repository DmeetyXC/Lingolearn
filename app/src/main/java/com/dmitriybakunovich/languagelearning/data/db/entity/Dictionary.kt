package com.dmitriybakunovich.languagelearning.data.db.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dictionary")
data class Dictionary(
    val dictionaryValue: String,
    val dictionaryTranslateValue: String
) {
    @PrimaryKey(autoGenerate = true)
    var idDictionary: Long = 0

    class DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem.idDictionary == newItem.idDictionary
        }

        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem == newItem
        }
    }
}