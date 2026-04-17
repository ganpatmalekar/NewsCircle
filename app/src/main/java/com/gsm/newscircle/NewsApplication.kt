package com.gsm.newscircle

import android.app.Application
import com.gsm.newscircle.di.component.ApplicationComponent
import com.gsm.newscircle.di.component.DaggerApplicationComponent
import com.gsm.newscircle.di.module.ApplicationModule

class NewsApplication : Application() {

    lateinit var daggerComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        injectDependencies()
    }

    private fun injectDependencies() {
        daggerComponent = DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()

        daggerComponent.inject(this)
    }
}