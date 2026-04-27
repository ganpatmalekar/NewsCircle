package com.gsm.newscircle.ui.pagination

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ItemTopHeadlineBinding
import com.gsm.newscircle.utils.ItemClickListener

class TopHeadlinePagingAdapter :
    PagingDataAdapter<ApiArticle, TopHeadlinePagingAdapter.TopHeadlineViewHolder>(
        ArticleDiffCallback()
    ) {

    lateinit var itemClickListener: ItemClickListener<Any>

    override fun onCreateViewHolder(
        parent: ViewGroup,
        position: Int
    ): TopHeadlineViewHolder = TopHeadlineViewHolder(
        ItemTopHeadlineBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(
        holder: TopHeadlineViewHolder,
        position: Int
    ) {
        val article = getItem(position)
        article?.let {
            holder.bind(it, itemClickListener)
        }
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
        fun bind(item: ApiArticle, itemClickListener: ItemClickListener<Any>) {
            binding.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description ?: "NA"
                tvSource.text = item.apiSource.name
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