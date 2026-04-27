package com.gsm.newscircle.ui.offline

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.newscircle.R
import com.gsm.newscircle.data.local.entity.Article
import com.gsm.newscircle.databinding.ActivityOfflineTopHeadlineBinding
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OfflineTopHeadlineActivity : AppCompatActivity() {

    lateinit var binding: ActivityOfflineTopHeadlineBinding
    private val topHeadlineOfflineViewModel: TopHeadlineOfflineViewModel by viewModels()
    @Inject
    lateinit var topHeadlineOfflineAdapter: TopHeadlineOfflineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOfflineTopHeadlineBinding.inflate(layoutInflater)
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
            tvTitle.text = getString(R.string.top_headlines_offline_architecture)
            ivNavBack.setOnClickListener {
                finish()
            }
        }
        binding.rvTopHeadlines.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@OfflineTopHeadlineActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = topHeadlineOfflineAdapter
        }
        binding.noInternetLayout.btnTryAgain.setOnClickListener {
            topHeadlineOfflineViewModel.fetchAllTopHeadlines()
        }

        topHeadlineOfflineAdapter.itemClickListener = { article ->
            openNewsOnBrowser(this@OfflineTopHeadlineActivity, article.url)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                topHeadlineOfflineViewModel.topHeadlineOfflineUiState.collect {
                    when (it) {
                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvTopHeadlines.visibility = View.VISIBLE
                            setupArticlesOnSuccess(it.data)
                        }

                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvTopHeadlines.visibility = View.GONE
                        }

                        is UiState.Error -> {
                            binding.progressBar.visibility = View.GONE
                            showLottieAnimation(it.message)
                        }
                    }
                }
            }
        }
    }

    fun setupArticlesOnSuccess(articles: List<Article>) {
        if (articles.isNotEmpty()) {
            binding.noArticlesLayout.clNoArticles.visibility = View.GONE
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            topHeadlineOfflineAdapter.submitList(articles)
        } else {
            showLottieAnimation(getString(R.string.no_articles_found_at_the_moment))
        }
    }

    private fun showLottieAnimation(errorMessage: String) {
        val isNoArticles = errorMessage.contains(AppConstants.NO_ARTICLES, true)
        binding.rvTopHeadlines.visibility = View.GONE
        if (!isNoArticles) {
            // Show no internet lottie animation
            binding.noArticlesLayout.clNoArticles.visibility = View.GONE
            binding.noInternetLayout.apply {
                clNoInternet.visibility = View.VISIBLE
                tvNoInternetText.text = errorMessage
            }
        } else {
            // Show no articles lottie animation
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            binding.noArticlesLayout.apply {
                clNoArticles.visibility = View.VISIBLE
                tvNoArticleFoundSubtitle.text = errorMessage
            }
        }
    }
}