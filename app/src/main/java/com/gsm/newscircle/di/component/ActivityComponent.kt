package com.gsm.newscircle.di.component

import com.gsm.newscircle.di.ActivityScope
import com.gsm.newscircle.di.module.ActivityModule
import com.gsm.newscircle.ui.topheadline.TopHeadlineActivity
import dagger.Component

@ActivityScope
@Component(modules = [ActivityModule::class], dependencies = [ApplicationComponent::class])
interface ActivityComponent {
    fun inject(activity: TopHeadlineActivity)
}