package com.gsm.newscircle.ui.source

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
import com.gsm.newscircle.data.model.newssource.ApiNewsSource
import com.gsm.newscircle.databinding.ActivityNewsSourceBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.news.NewsListActivity
import com.gsm.newscircle.utils.AppConstants
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsSourceActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsSourceBinding

    @Inject
    lateinit var newsSourceViewModel: NewsSourceViewModel

    @Inject
    lateinit var newsSourceAdapter: NewsSourceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivityNewsSourceBinding.inflate(layoutInflater)
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
            tvTitle.text = getString(R.string.news_sources)
            ivNavBack.setOnClickListener {
                finish()
            }
        }
        binding.rvNewsSource.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@NewsSourceActivity)
            addItemDecoration(
                DividerItemDecoration(
                    this.context, (layoutManager as LinearLayoutManager).orientation
                )
            )
            adapter = newsSourceAdapter
        }
        binding.noInternetLayout.btnTryAgain.setOnClickListener {
            newsSourceViewModel.fetchAllNewsSources()
        }
        newsSourceAdapter.itemClickListener = { newsSource ->
            startActivity(
                NewsListActivity.getStartIntent(
                    context = this,
                    newsSource = newsSource.id,
                    newsType = AppConstants.NEWS_BY_SOURCES
                )
            )
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                newsSourceViewModel.newsSourceUiState.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.progressBar.visibility = View.VISIBLE
                            binding.rvNewsSource.visibility = View.GONE
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvNewsSource.visibility = View.VISIBLE
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

    fun setupArticlesOnSuccess(sources: List<ApiNewsSource>) {
        if (sources.isNotEmpty()) {
            binding.clNoSources.visibility = View.GONE
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            newsSourceAdapter.submitList(sources)
        } else {
            showLottieAnimation(getString(R.string.no_sources_found_at_the_moment))
        }
    }

    fun showLottieAnimation(errorMessage: String) {
        val isNoSources = errorMessage.contains(AppConstants.NO_SOURCES, true)
        binding.rvNewsSource.visibility = View.GONE
        if (!isNoSources) {
            // Show no internet lottie animation
            binding.clNoSources.visibility = View.GONE
            binding.noInternetLayout.apply {
                clNoInternet.visibility = View.VISIBLE
                tvNoInternetText.text = errorMessage
            }
        } else {
            // Show no sources lottie animation
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            binding.clNoSources.visibility = View.VISIBLE
            binding.tvNoSourcesFoundSubtitle.text = errorMessage
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