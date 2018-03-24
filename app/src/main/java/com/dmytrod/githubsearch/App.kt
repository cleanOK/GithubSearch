package com.dmytrod.githubsearch

import android.app.Application

/**
 * Created by Dmytro Denysenko on 10.02.18.
 */
class App : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(ApplicationModule(this))
                .build()
        applicationComponent
                .inject(this)
    }
}