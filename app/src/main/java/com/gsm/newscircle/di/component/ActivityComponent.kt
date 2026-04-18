package com.gsm.newscircle.di.component

import com.gsm.newscircle.di.ActivityScope
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.country.CountryListBottomSheet
import com.gsm.newscircle.ui.country.NewsByCountryActivity
import com.gsm.newscircle.ui.language.LanguageListBottomSheet
import com.gsm.newscircle.ui.language.NewsByLanguageActivity
import com.gsm.newscircle.ui.news.NewsListActivity
import com.gsm.newscircle.ui.source.NewsSourceActivity
import com.gsm.newscircle.ui.topheadline.TopHeadlineActivity
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {
    fun inject(activity: TopHeadlineActivity)
    fun inject(activity: NewsSourceActivity)
    fun inject(activity: NewsListActivity)
    fun inject(activity: NewsByCountryActivity)
    fun inject(fragment: CountryListBottomSheet)
    fun inject(activity: NewsByLanguageActivity)
    fun inject(fragment: LanguageListBottomSheet)
}