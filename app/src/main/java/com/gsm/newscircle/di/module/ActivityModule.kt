package com.gsm.newscircle.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gsm.newscircle.data.repository.CountryListRepository
import com.gsm.newscircle.data.repository.LanguageListRepository
import com.gsm.newscircle.data.repository.NewsListRepository
import com.gsm.newscircle.data.repository.NewsSourceRepository
import com.gsm.newscircle.data.repository.SearchNewsRepository
import com.gsm.newscircle.data.repository.TopHeadlineOfflineRepository
import com.gsm.newscircle.data.repository.TopHeadlinePagingRepository
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.di.ActivityContext
import com.gsm.newscircle.ui.base.ViewModelProviderFactory
import com.gsm.newscircle.ui.country.CountryListAdapter
import com.gsm.newscircle.ui.country.CountryListViewModel
import com.gsm.newscircle.ui.language.LanguageListAdapter
import com.gsm.newscircle.ui.language.LanguageListViewModel
import com.gsm.newscircle.ui.news.NewsListViewModel
import com.gsm.newscircle.ui.offline.TopHeadlineOfflineAdapter
import com.gsm.newscircle.ui.offline.TopHeadlineOfflineViewModel
import com.gsm.newscircle.ui.pagination.TopHeadlinePaginationViewModel
import com.gsm.newscircle.ui.pagination.TopHeadlinePagingAdapter
import com.gsm.newscircle.ui.search.SearchNewsViewModel
import com.gsm.newscircle.ui.search.SortOptionsAdapter
import com.gsm.newscircle.ui.source.NewsSourceAdapter
import com.gsm.newscircle.ui.source.NewsSourceViewModel
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import com.gsm.newscircle.ui.topheadline.TopHeadlineViewModel
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import dagger.Module
import dagger.Provides
import kotlin.jvm.java

@Module
class ActivityModule(private val mActivity: AppCompatActivity) {

    @ActivityContext
    @Provides
    fun providesContext(): Context = mActivity

    @Provides
    fun providesTopHeadlineViewModel(
        topHeadlineRepository: TopHeadlineRepository,
        dispatcherProvider: DispatcherProvider,
        networkHelper: NetworkHelper,
        loggerService: LoggerService
    ): TopHeadlineViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(TopHeadlineViewModel::class) {
                TopHeadlineViewModel(
                    topHeadlineRepository,
                    dispatcherProvider,
                    networkHelper,
                    loggerService
                )
            }
        )[TopHeadlineViewModel::class.java]
    }

    @Provides
    fun providesNewsSourceViewModel(
        newsSourceRepository: NewsSourceRepository,
        dispatcherProvider: DispatcherProvider,
        networkHelper: NetworkHelper,
        loggerService: LoggerService
    ): NewsSourceViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(NewsSourceViewModel::class) {
                NewsSourceViewModel(
                    newsSourceRepository,
                    dispatcherProvider,
                    networkHelper,
                    loggerService
                )
            }
        )[NewsSourceViewModel::class.java]
    }

    @Provides
    fun providesNewsListViewModel(
        newsListRepository: NewsListRepository,
        dispatcherProvider: DispatcherProvider,
        networkHelper: NetworkHelper,
        loggerService: LoggerService
    ): NewsListViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(NewsListViewModel::class) {
                NewsListViewModel(
                    newsListRepository,
                    dispatcherProvider,
                    networkHelper,
                    loggerService
                )
            }
        )[NewsListViewModel::class.java]
    }

    @Provides
    fun providesCountryListViewModel(
        countryListRepository: CountryListRepository,
        dispatcherProvider: DispatcherProvider,
        loggerService: LoggerService
    ): CountryListViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(CountryListViewModel::class) {
                CountryListViewModel(
                    countryListRepository,
                    dispatcherProvider,
                    loggerService
                )
            }
        )[CountryListViewModel::class.java]
    }

    @Provides
    fun providesLanguageListViewModel(
        languageListRepository: LanguageListRepository,
        dispatcherProvider: DispatcherProvider,
        loggerService: LoggerService
    ): LanguageListViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(LanguageListViewModel::class) {
                LanguageListViewModel(
                    languageListRepository,
                    dispatcherProvider,
                    loggerService
                )
            }
        )[LanguageListViewModel::class.java]
    }

    @Provides
    fun providesSearchNewsViewModel(
        searchNewsRepository: SearchNewsRepository,
        dispatcherProvider: DispatcherProvider,
        networkHelper: NetworkHelper,
        loggerService: LoggerService
    ): SearchNewsViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(SearchNewsViewModel::class) {
                SearchNewsViewModel(
                    searchNewsRepository,
                    dispatcherProvider,
                    networkHelper,
                    loggerService
                )
            }
        )[SearchNewsViewModel::class.java]
    }

    @Provides
    fun providesTopHeadlineOfflineViewModel(
        topHeadlineOfflineRepository: TopHeadlineOfflineRepository,
        dispatcherProvider: DispatcherProvider,
        networkHelper: NetworkHelper,
        loggerService: LoggerService
    ): TopHeadlineOfflineViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(TopHeadlineOfflineViewModel::class) {
                TopHeadlineOfflineViewModel(
                    topHeadlineOfflineRepository,
                    dispatcherProvider,
                    networkHelper,
                    loggerService
                )
            }
        )[TopHeadlineOfflineViewModel::class.java]
    }

    @Provides
    fun providesTopHeadlinePaginationViewModel(
        topHeadlinePagingRepository: TopHeadlinePagingRepository
    ): TopHeadlinePaginationViewModel {
        return ViewModelProvider(
            mActivity,
            ViewModelProviderFactory(TopHeadlinePaginationViewModel::class) {
                TopHeadlinePaginationViewModel(
                    topHeadlinePagingRepository
                )
            }
        )[TopHeadlinePaginationViewModel::class.java]
    }

    @Provides
    fun providesTopHeadlineAdapter() = TopHeadlineAdapter()

    @Provides
    fun providesNewsSourceAdapter() = NewsSourceAdapter()

    @Provides
    fun providesCountryListAdapter() = CountryListAdapter()

    @Provides
    fun providesLanguageListAdapter() = LanguageListAdapter()

    @Provides
    fun providesSortOptionAdapter() = SortOptionsAdapter()

    @Provides
    fun providesTopHeadlineOfflineAdapter() = TopHeadlineOfflineAdapter()

    @Provides
    fun providesTopHeadlinePagingAdapter() = TopHeadlinePagingAdapter()
}