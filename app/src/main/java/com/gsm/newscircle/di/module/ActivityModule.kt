package com.gsm.newscircle.di.module

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.di.ActivityContext
import com.gsm.newscircle.ui.base.ViewModelProviderFactory
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
    fun providesTopHeadlineAdapter() = TopHeadlineAdapter()
}