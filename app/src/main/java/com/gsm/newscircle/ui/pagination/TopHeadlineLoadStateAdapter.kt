package com.gsm.newscircle.ui.pagination

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gsm.newscircle.databinding.LayoutLoadStateFooterBinding

class TopHeadlineLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<TopHeadlineLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = LayoutLoadStateFooterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LoadStateViewHolder(binding, retry)
    }

    class LoadStateViewHolder(
        private val binding: LayoutLoadStateFooterBinding,
        private val retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.apply {
                if (loadState is LoadState.Error) {
                    tvErrorMessage.text = loadState.error.localizedMessage
                }
                progressBar.visibility =
                    if (loadState is LoadState.Loading) View.VISIBLE else View.GONE
                btnRetry.visibility =
                    if (loadState is LoadState.Error) View.VISIBLE else View.GONE
                tvErrorMessage.visibility =
                    if (loadState is LoadState.Error) View.VISIBLE else View.GONE

                btnRetry.setOnClickListener {
                    retry()
                }
            }
        }
    }
}