package com.gsm.newscircle.di.component

import android.content.Context
import com.gsm.newscircle.NewsApplication
import com.gsm.newscircle.data.repository.CountryListRepository
import com.gsm.newscircle.data.repository.NewsListRepository
import com.gsm.newscircle.data.repository.NewsSourceRepository
import com.gsm.newscircle.data.repository.TopHeadlineRepository
import com.gsm.newscircle.di.ApplicationContext
import com.gsm.newscircle.di.module.ApplicationModule
import com.gsm.newscircle.utils.DispatcherProvider
import com.gsm.newscircle.utils.NetworkHelper
import com.gsm.newscircle.utils.logger.LoggerService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NewsApplication)

    @ApplicationContext
    fun getContext(): Context

    fun getTopHeadlineRepository(): TopHeadlineRepository
    fun getNewsSourceRepository(): NewsSourceRepository
    fun getNewsListRepository(): NewsListRepository
    fun getCountryListRepository(): CountryListRepository

    fun getDispatcherProvider(): DispatcherProvider

    fun getNetworkHelper(): NetworkHelper

    fun getLoggerService(): LoggerService
}