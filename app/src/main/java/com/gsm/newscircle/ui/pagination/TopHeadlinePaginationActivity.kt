package com.gsm.newscircle.ui.pagination

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ActivityTopHeadlinePaginationBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.utils.Helper.handleError
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadlinePaginationActivity : AppCompatActivity() {

    lateinit var binding: ActivityTopHeadlinePaginationBinding

    @Inject
    lateinit var topHeadlinePaginationViewModel: TopHeadlinePaginationViewModel

    @Inject
    lateinit var topHeadlinePagingAdapter: TopHeadlinePagingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivityTopHeadlinePaginationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.toolbarLayout.apply {
            tvTitle.text = getString(R.string.top_headlines_pagination)
            ivNavBack.setOnClickListener {
                finish()
            }
        }
        binding.rvTopHeadlines.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TopHeadlinePaginationActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = topHeadlinePagingAdapter.withLoadStateFooter(
                footer = TopHeadlineLoadStateAdapter { topHeadlinePagingAdapter.retry() }
            )
        }
        binding.noInternetLayout.btnTryAgain.setOnClickListener {
            topHeadlinePagingAdapter.retry()
        }

        topHeadlinePagingAdapter.itemClickListener = { data ->
            val article = data as ApiArticle
            openNewsOnBrowser(this@TopHeadlinePaginationActivity, article.url)
        }

        topHeadlinePagingAdapter.addLoadStateListener { loadState ->
            binding.apply {
                val isListEmpty =
                    loadState.refresh is LoadState.NotLoading && topHeadlinePagingAdapter.itemCount == 0
                val isError = loadState.refresh is LoadState.Error

                progressBar.isVisible = loadState.refresh is LoadState.Loading
                rvTopHeadlines.isVisible = !isListEmpty && !isError
                noArticlesLayout.clNoArticles.isVisible = isListEmpty
                noInternetLayout.clNoInternet.isVisible = isError

                if (isError) {
                    val error = (loadState.refresh as LoadState.Error).error
                    noInternetLayout.tvNoInternetText.text = handleError(error)
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                topHeadlinePaginationViewModel.topHeadlineUiState.collectLatest { pagingData ->
                    topHeadlinePagingAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).daggerComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }
}