package com.gsm.newscircle.ui.topheadline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ItemTopHeadlineBinding
import com.gsm.newscircle.utils.ItemClickListener

class TopHeadlineAdapter :
    ListAdapter<ApiArticle, TopHeadlineAdapter.TopHeadlineViewHolder>(ArticleDiffCallback()) {

    lateinit var itemClickListener: ItemClickListener<ApiArticle>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): TopHeadlineViewHolder {
        return TopHeadlineViewHolder(
            ItemTopHeadlineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: TopHeadlineViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position), itemClickListener)
    }

    class ArticleDiffCallback : DiffUtil.ItemCallback<ApiArticle>() {
        override fun areItemsTheSame(
            old: ApiArticle,
            new: ApiArticle
        ): Boolean = old.url == new.url

        override fun areContentsTheSame(
            old: ApiArticle,
            new: ApiArticle
        ): Boolean = old == new
    }

    class TopHeadlineViewHolder(val binding: ItemTopHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ApiArticle, itemClickListener: ItemClickListener<ApiArticle>) {
            binding.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description
                tvSource.text = item.apiSource.name
                tvAuthor.text = item.author
                Glide.with(imageViewBanner.context)
                    .load(item.urlToImage)
                    .placeholder(R.drawable.ic_news_placeholder)
                    .fallback(R.drawable.ic_news_placeholder)
                    .into(imageViewBanner)
                root.setOnClickListener {
                    itemClickListener.invoke(item)
                }
            }
        }
    }
}