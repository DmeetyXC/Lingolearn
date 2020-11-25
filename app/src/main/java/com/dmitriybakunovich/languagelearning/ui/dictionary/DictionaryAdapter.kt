package com.dmitriybakunovich.languagelearning.ui.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmitriybakunovich.languagelearning.data.entity.Dictionary
import com.dmitriybakunovich.languagelearning.databinding.ItemDictionaryBinding

class DictionaryAdapter :
    ListAdapter<Dictionary, DictionaryAdapter.DictionaryViewHolder>(Dictionary.DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DictionaryViewHolder =
        DictionaryViewHolder(
            ItemDictionaryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: DictionaryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DictionaryViewHolder(binding: ItemDictionaryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val dictionaryValue = binding.dictionaryValue
        private val dictionaryTranslateValue = binding.dictionaryTranslateValue

        fun bind(dictionary: Dictionary) {
            dictionaryValue.text = dictionary.dictionaryValue
            dictionaryTranslateValue.text = dictionary.dictionaryTranslateValue
        }
    }
}