package com.gsm.newscircle.ui.country

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ActivityNewsByCountryBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.news.NewsListViewModel
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewsByCountryActivity : AppCompatActivity() {

    lateinit var binding: ActivityNewsByCountryBinding
    @Inject
    lateinit var newsListViewModel: NewsListViewModel
    @Inject
    lateinit var countryListViewModel: CountryListViewModel
    @Inject
    lateinit var topHeadlineAdapter: TopHeadlineAdapter
    private var selectedCountryCode = AppConstants.COUNTRY

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivityNewsByCountryBinding.inflate(layoutInflater)
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
        binding.apply {
            toolbarLayout.apply {
                tvTitle.text = getString(R.string.country_selection)
                ivNavBack.setOnClickListener {
                    finish()
                }
            }
            clSelectCountry.setOnClickListener {
                val bottomSheet = CountryListBottomSheet(selectedCountryCode)
                bottomSheet.show(supportFragmentManager, bottomSheet.tag)
            }
            rvNews.apply {
                layoutManager = LinearLayoutManager(this@NewsByCountryActivity)
                setHasFixedSize(true)
                adapter = topHeadlineAdapter
            }
        }
        topHeadlineAdapter.itemClickListener = { article ->
            openNewsOnBrowser(this@NewsByCountryActivity, article.url)
        }
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    countryListViewModel.selectedCountry.collect { country ->
                        newsListViewModel.fetchAllNewsByCountry(country.code)
                        binding.tvSelectedCountry.text = country.name
                        selectedCountryCode = country.code
                    }
                }
                launch {
                    observeFetchNewsByCountry()
                }
            }
        }
    }

    private suspend fun observeFetchNewsByCountry() {
        newsListViewModel.newsUiState.collect {
            when (it) {
                UiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvNews.visibility = View.GONE
                    binding.noArticlesLayout.clNoArticles.visibility = View.GONE
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

    private fun setupArticlesOnSuccess(articles: List<ApiArticle>) {
        if (articles.isNotEmpty()) {
            binding.noArticlesLayout.clNoArticles.visibility = View.GONE
            binding.noInternetLayout.clNoInternet.visibility = View.GONE
            topHeadlineAdapter.submitList(articles)
        } else {
            showLottieAnimation(
                getString(
                    R.string.no_articles_found_subtitle,
                    countryListViewModel.selectedCountry.value.name
                )
            )
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