package com.dmitriybakunovich.languagelearning.ui.dictionary

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.R
import com.dmitriybakunovich.languagelearning.data.db.entity.Dictionary
import kotlinx.android.synthetic.main.item_dictionary.view.*

class DictionaryAdapter :
    ListAdapter<Dictionary, DictionaryAdapter.DictionaryViewHolder>(Dictionary.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder =
        DictionaryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_dictionary, parent, false
            )
        )

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DictionaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dictionaryValue = itemView.dictionaryValue
        private val dictionaryTranslateValue = itemView.dictionaryTranslateValue

        fun bind(dictionary: Dictionary) {
            dictionaryValue.text = dictionary.dictionaryValue
            dictionaryTranslateValue.text = dictionary.dictionaryTranslateValue
        }
    }
}