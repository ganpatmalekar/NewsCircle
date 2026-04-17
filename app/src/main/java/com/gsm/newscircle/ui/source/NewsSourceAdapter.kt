package com.gsm.newscircle.ui.source

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.newscircle.data.model.newssource.ApiNewsSource
import com.gsm.newscircle.databinding.ItemNewsSourceBinding
import com.gsm.newscircle.utils.ItemClickListener

class NewsSourceAdapter :
    ListAdapter<ApiNewsSource, NewsSourceAdapter.NewsSourceViewHolder>(NewsSourceDiffCallback()) {

    lateinit var itemClickListener: ItemClickListener<ApiNewsSource>

    class NewsSourceDiffCallback : DiffUtil.ItemCallback<ApiNewsSource>() {
        override fun areItemsTheSame(
            old: ApiNewsSource,
            new: ApiNewsSource
        ): Boolean = old.id == new.id

        override fun areContentsTheSame(
            old: ApiNewsSource,
            new: ApiNewsSource
        ): Boolean = old == new
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): NewsSourceViewHolder {
        return NewsSourceViewHolder(
            ItemNewsSourceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: NewsSourceViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    class NewsSourceViewHolder(val binding: ItemNewsSourceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApiNewsSource, itemClickListener: ItemClickListener<ApiNewsSource>) {
            binding.apply {
                tvTitle.text = item.name
                tvDescription.text = item.description
                tvCategory.text = item.category
                root.setOnClickListener {
                    itemClickListener.invoke(item)
                }
            }
        }
    }
}