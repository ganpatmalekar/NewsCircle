package com.gsm.newscircle.ui.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.Language
import com.gsm.newscircle.databinding.ItemLanguageBinding
import com.gsm.newscircle.utils.ItemClickListener

class LanguageListAdapter :
    ListAdapter<Language, LanguageListAdapter.LanguageViewHolder>(LanguageDiffCallback()) {

    lateinit var itemClickListener: ItemClickListener<Language>
    var selectedLanguageCode: String = ""

    fun updateData(newList: List<Language>, newSelection: String) {
        selectedLanguageCode = newSelection
        submitList(newList)
    }

    class LanguageDiffCallback : DiffUtil.ItemCallback<Language>() {
        override fun areItemsTheSame(
            old: Language,
            new: Language
        ): Boolean = old.code == new.code

        override fun areContentsTheSame(
            old: Language,
            new: Language
        ): Boolean = old == new
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): LanguageViewHolder {
        return LanguageViewHolder(
            ItemLanguageBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: LanguageViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(language: Language, itemClickListener: ItemClickListener<Language>) {
            binding.apply {
                tvLanguageName.text = language.name
                // Change background color based on selection
                if (language.code == selectedLanguageCode) {
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.lightSkyBlue
                        )
                    )
                } else {
                    itemView.setBackgroundColor(
                        ContextCompat.getColor(
                            itemView.context,
                            android.R.color.transparent
                        )
                    )
                }
                root.setOnClickListener {
                    itemClickListener(language)
                }
            }
        }
    }
}