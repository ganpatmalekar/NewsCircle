package com.gsm.newscircle.ui.news

import android.content.Context
import android.content.Intent
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
import com.gsm.newscircle.databinding.ActivityNewsListBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsListActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsListBinding

    @Inject
    lateinit var newsListViewModel: NewsListViewModel

    @Inject
    lateinit var topHeadlineAdapter: TopHeadlineAdapter
    private var sourceId: String? = null

    companion object {
        private const val EXTRA_NEWS_SOURCE = "EXTRA_NEWS_SOURCE"
        private const val EXTRA_NEWS_TYPE = "EXTRA_NEWS_TYPE"

        fun getStartIntent(context: Context, newsSource: String? = "", newsType: String): Intent {
            return Intent(context, NewsListActivity::class.java).apply {
                putExtra(EXTRA_NEWS_TYPE, newsType)
                putExtra(EXTRA_NEWS_SOURCE, newsSource)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivityNewsListBinding.inflate(layoutInflater)
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
            tvTitle.text = getString(R.string.news_by_sources)
            ivNavBack.setOnClickListener {
                finish()
            }
        }
        binding.rvNews.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NewsListActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context, (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = topHeadlineAdapter
        }
        topHeadlineAdapter.itemClickListener = { article ->
            openNewsOnBrowser(this@NewsListActivity, article.url)
        }
        binding.noInternetLayout.btnTryAgain.setOnClickListener {
            getIntentData()
        }

        getIntentData()
    }

    private fun getIntentData() {
        val newsType = intent.getStringExtra(EXTRA_NEWS_TYPE)
        newsType?.let { type ->
            when (type) {
                AppConstants.NEWS_BY_SOURCES -> {
                    val source = intent.getStringExtra(EXTRA_NEWS_SOURCE)
                    // Assigned source to top-level sourceId
                    sourceId = source
                    source?.let {
                        newsListViewModel.fetchAllNewsBySources(it)
                    }
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsListViewModel.newsUiState.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvNews.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvNews.visibility = View.VISIBLE
                            setupArticlesOnSuccess(it.data)
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

    fun setupArticlesOnSuccess(articles: List<ApiArticle>) {
        if (articles.isNotEmpty()) {
            binding.noArticlesLayout.clNoArticles.visibility = View.GONE
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            topHeadlineAdapter.submitList(articles)
        } else {
            showLottieAnimation(getString(R.string.no_articles_found_subtitle, sourceId ?: "NA"))
        }
    }

    fun showLottieAnimation(errorMessage: String) {
        val isNoArticles = errorMessage.contains(AppConstants.NO_ARTICLES, true)
        binding.rvNews.visibility = View.GONE
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

    private fun injectDependencies() {
        DaggerActivityComponent.builder()
            .applicationComponent((application as NewsApplication).daggerComponent)
            .activityModule(ActivityModule(this))
            .build()
            .inject(this)
    }
}