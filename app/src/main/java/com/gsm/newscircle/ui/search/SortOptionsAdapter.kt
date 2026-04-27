package com.gsm.newscircle.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.newscircle.data.model.SortOption
import com.gsm.newscircle.databinding.ItemSortOptionBinding
import com.gsm.newscircle.utils.ItemClickListener

class SortOptionsAdapter :
    ListAdapter<SortOption, SortOptionsAdapter.SortOptionViewHolder>(SortOptionDiffCallback()) {

    lateinit var itemClickListener: ItemClickListener<SortOption>

    class SortOptionDiffCallback : DiffUtil.ItemCallback<SortOption>() {
        override fun areItemsTheSame(
            old: SortOption,
            new: SortOption
        ): Boolean = old.title == new.title

        override fun areContentsTheSame(
            old: SortOption,
            new: SortOption
        ): Boolean = old == new
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): SortOptionViewHolder {
        return SortOptionViewHolder(
            ItemSortOptionBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: SortOptionViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    class SortOptionViewHolder(val binding: ItemSortOptionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(sortOption: SortOption, itemClickListener: ItemClickListener<SortOption>) {
            binding.apply {
                tvTitle.text = sortOption.title
                tvDesc.text = sortOption.desc
                ivTick.visibility = if (sortOption.isSelected) View.VISIBLE else View.GONE

                root.setOnClickListener {
                    itemClickListener(sortOption)
                }
            }
        }
    }
}