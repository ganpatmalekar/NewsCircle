package com.gsm.newscircle.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gsm.newscircle.data.repository.NewsListRepository
import com.gsm.newscircle.data.repository.NewsSourceRepository
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.di.ActivityContext
import com.gsm.newscircle.ui.base.ViewModelProviderFactory
import com.gsm.newscircle.ui.news.NewsListViewModel
import com.gsm.newscircle.ui.source.NewsSourceAdapter
import com.gsm.newscircle.ui.source.NewsSourceViewModel
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import com.gsm.newscircle.ui.topheadline.TopHeadlineViewModel
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import dagger.Module
import dagger.Provides

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
    fun providesTopHeadlineAdapter() = TopHeadlineAdapter()

    @Provides
    fun providesNewsSourceAdapter() = NewsSourceAdapter()
}