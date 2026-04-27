package com.gsm.newscircle.ui.offline

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsm.newscircle.R
import com.gsm.newscircle.data.local.entity.Article
import com.gsm.newscircle.databinding.ItemTopHeadlineBinding
import com.gsm.newscircle.utils.ItemClickListener

class TopHeadlineOfflineAdapter :
    ListAdapter<Article, TopHeadlineOfflineAdapter.TopHeadlineViewHolder>(ArticleDiffCallback()) {

    lateinit var itemClickListener: ItemClickListener<Article>

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

    class ArticleDiffCallback : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(
            old: Article,
            new: Article
        ): Boolean = old.url == new.url

        override fun areContentsTheSame(
            old: Article,
            new: Article
        ): Boolean = old == new
    }

    class TopHeadlineViewHolder(val binding: ItemTopHeadlineBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Article, itemClickListener: ItemClickListener<Article>) {
            binding.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description ?: "NA"
                tvSource.text = item.source.name
                tvAuthor.text = item.author?.trim() ?: "NA"
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