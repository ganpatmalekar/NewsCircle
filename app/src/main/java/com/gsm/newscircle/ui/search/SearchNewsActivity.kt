package com.gsm.newscircle.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.R
import com.gsm.newscircle.data.model.topheadline.ApiArticle
import com.gsm.newscircle.databinding.ActivitySearchNewsBinding
import com.gsm.newscircle.databinding.LayoutSortOptionPopupBinding
import com.gsm.newscircle.di.component.DaggerActivityComponent
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.base.UiState
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import com.gsm.newscircle.utils.AppConstants
import com.gsm.newscircle.utils.Helper.openNewsOnBrowser
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchNewsActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchNewsBinding

    @Inject
    lateinit var searchNewsViewModel: SearchNewsViewModel

    @Inject
    lateinit var topHeadlineAdapter: TopHeadlineAdapter

    @Inject
    lateinit var sortOptionsAdapter: SortOptionsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        injectDependencies()
        super.onCreate(savedInstanceState)

        binding = ActivitySearchNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setupUI()
        setupObserver()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI() {
        binding.apply {
            toolbarLayout.apply {
                ivNavBack.setOnClickListener {
                    finish()
                }
                ivSortByFilter.setOnClickListener {
                    // Open sortByOption pop-up menu
                    handleSortByOptions(it)
                }
            }

            rvSearchedNews.setHasFixedSize(true)
            rvSearchedNews.adapter = topHeadlineAdapter
            topHeadlineAdapter.itemClickListener = { article ->
                openNewsOnBrowser(this@SearchNewsActivity, article.url)
            }

            // Initial state: hide the close icon
            etSearchNews.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)

            etSearchNews.addTextChangedListener { editable ->
                val query = editable.toString()
                if (query.isNotEmpty()) {
                    etSearchNews.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_search,
                        0,
                        R.drawable.ic_close,
                        0
                    )
                } else {
                    etSearchNews.setCompoundDrawablesWithIntrinsicBounds(
                        R.drawable.ic_search,
                        0,
                        0,
                        0
                    )
                }
                searchNewsViewModel.searchNewsByQuery(query)
            }

            etSearchNews.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_UP) {
                    val drawableEnd = etSearchNews.compoundDrawables[2]
                    if (drawableEnd != null) {
                        if (event.rawX >= (etSearchNews.right - etSearchNews.paddingEnd - drawableEnd.bounds.width())) {
                            etSearchNews.text?.clear()
                            return@setOnTouchListener true
                        }
                    }
                }
                false
            }
        }
    }

    fun handleSortByOptions(anchor: View) {
        val sortOptionList = searchNewsViewModel.getSortOptions()

        val popupBinding = LayoutSortOptionPopupBinding.inflate(layoutInflater)

        // Set a fixed width for the popup to avoid zero-width issues with WRAP_CONTENT
        val width = (resources.displayMetrics.widthPixels * 0.6).toInt() // 60% of screen width

        val popupWindow = PopupWindow(
            popupBinding.root,
            width,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        popupWindow.elevation = 20f
        popupWindow.isOutsideTouchable = true

        popupBinding.rvSortOptions.apply {
            setHasFixedSize(true)
            adapter = sortOptionsAdapter
        }
        sortOptionsAdapter.submitList(sortOptionList)
        sortOptionsAdapter.itemClickListener = { sortOption ->
            // Handle sort option click
            searchNewsViewModel.setSortByOption(sortOption.key)
            popupWindow.dismiss()
        }

        popupWindow.showAsDropDown(anchor, -200, 20)
    }

    private fun setupObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchNewsViewModel.searchUiState.collect {
                    when (it) {
                        UiState.Loading -> {
                            binding.apply {
                                progressBar.visibility = View.VISIBLE
                                rvSearchedNews.visibility = View.GONE
                                noArticlesLayout.clNoArticles.visibility = View.GONE
                            }
                        }

                        is UiState.Success -> {
                            binding.progressBar.visibility = View.GONE
                            binding.rvSearchedNews.visibility = View.VISIBLE
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
            showLottieAnimation(
                getString(
                    R.string.no_articles_found_subtitle,
                    binding.etSearchNews.text.toString()
                )
            )
        }
    }

    fun showLottieAnimation(errorMessage: String) {
        val isNoArticles = errorMessage.contains(AppConstants.NO_ARTICLES, true)
        binding.rvSearchedNews.visibility = View.GONE
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