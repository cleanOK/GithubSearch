package com.dmytrod.githubsearch

import com.dmytrod.githubsearch.repositories.AuthRepository
import com.dmytrod.githubsearch.repositories.SearchRepository
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Dmytro Denysenko on 10.02.18.
 */
@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {
    fun inject(app: App)

    fun provideAuthRepository() : AuthRepository

    fun provideSearchRepository() : SearchRepository
}