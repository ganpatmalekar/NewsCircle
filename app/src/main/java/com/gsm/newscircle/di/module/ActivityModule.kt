package com.gsm.newscircle.di.module

import com.gsm.newscircle.ui.country.CountryListAdapter
import com.gsm.newscircle.ui.language.LanguageListAdapter
import com.gsm.newscircle.ui.offline.TopHeadlineOfflineAdapter
import com.gsm.newscircle.ui.pagination.TopHeadlinePagingAdapter
import com.gsm.newscircle.ui.search.SortOptionsAdapter
import com.gsm.newscircle.ui.source.NewsSourceAdapter
import com.gsm.newscircle.ui.topheadline.TopHeadlineAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

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