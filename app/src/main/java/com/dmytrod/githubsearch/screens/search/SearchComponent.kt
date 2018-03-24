package com.dmytrod.githubsearch.screens.search

import com.dmytrod.githubsearch.ApplicationComponent
import com.dmytrod.githubsearch.di.PerActivity
import dagger.Component

/**
 * Created by Dmytro Denysenko on 10.02.18.
 */
@PerActivity
@Component(dependencies = [ApplicationComponent::class],
        modules = [SearchModule::class])
interface SearchComponent {
    fun inject(activity: SearchActivity)

}