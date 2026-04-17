package com.gsm.newscircle.ui.topheadline

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ActivityTopHeadlineBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import kotlinx.coroutines.launch
import javax.inject.Inject

class TopHeadlineActivity : AppCompatActivity() {

    lateinit var binding: ActivityTopHeadlineBinding

    @Inject
    lateinit var viewModel: TopHeadlineViewModel

    @Inject
    lateinit var topHeadlineAdapter: TopHeadlineAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivityTopHeadlineBinding.inflate(layoutInflater)
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
            tvTitle.text = getString(R.string.top_headlines)
            ivNavBack.setOnClickListener {
                finish()
            }
        }
        binding.rvTopHeadlines.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@TopHeadlineActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context,
                    (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = topHeadlineAdapter
        }
        binding.btnTryAgain.setOnClickListener {
            viewModel.fetchTopHeadlines()
        }

        topHeadlineAdapter.itemClickListener = { article ->
            openNewsOnBrowser(this@TopHeadlineActivity, article.url)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.topHeadlineUiState.collect {
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
                            binding.rvTopHeadlines.visibility = View.GONE
                            showLottieAnimation(it.message)
                        }
                    }
                }
            }
        }
    }

    fun setupArticlesOnSuccess(articles: List<ApiArticle>) {
        if (articles.isNotEmpty()) {
            binding.clNoArticles.visibility = View.GONE
            binding.clNoInternet.visibility = View.GONE
            binding.rvTopHeadlines.visibility = View.VISIBLE
            topHeadlineAdapter.submitList(articles)
        } else {
            showLottieAnimation(getString(R.string.no_articles_found_at_the_moment))
        }
    }

    private fun showLottieAnimation(errorMessage: String) {
        val isNoArticles = errorMessage.contains(AppConstants.NO_ARTICLES, true)
        binding.rvTopHeadlines.visibility = View.GONE
        if (!isNoArticles) {
            // Show no internet lottie animation
            binding.clNoArticles.visibility = View.GONE
            binding.clNoInternet.visibility = View.VISIBLE
            binding.tvNoInternetText.text = errorMessage
        } else {
            // Show no articles lottie animation
            binding.clNoInternet.visibility = View.GONE
            binding.clNoArticles.visibility = View.VISIBLE
            binding.tvNoArticleFoundSubtitle.text = errorMessage
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