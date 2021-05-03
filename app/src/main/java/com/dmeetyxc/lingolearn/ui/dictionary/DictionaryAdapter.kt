package com.dmeetyxc.lingolearn.ui.dictionary

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dmeetyxc.lingolearn.data.entity.Dictionary
import com.dmeetyxc.lingolearn.databinding.ItemDictionaryBinding

class DictionaryAdapter :
    ListAdapter<Dictionary, DictionaryAdapter.DictionaryViewHolder>(DiffCallback()) {

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

    class DiffCallback : DiffUtil.ItemCallback<Dictionary>() {
        override fun areItemsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem.idDictionary == newItem.idDictionary
        }

        override fun areContentsTheSame(oldItem: Dictionary, newItem: Dictionary): Boolean {
            return oldItem == newItem
        }
    }
}