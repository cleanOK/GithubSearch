package com.dmytrod.githubsearch.screens.search

import com.dmytrod.githubsearch.di.PerActivity
import com.dmytrod.githubsearch.repositories.AuthRepository
import com.dmytrod.githubsearch.repositories.SearchRepository
import dagger.Module
import dagger.Provides

/**
 * Created by Dmytro Denysenko on 10.02.18.
 */
@Module
class SearchModule {

    @Provides
    @PerActivity
    fun providePresenter(loginUseCase: LoginUseCase, searchUseCase: SearchUseCase): SearchContract.Presenter {
        return SearchPresenter(loginUseCase, searchUseCase)
    }

    @Provides
    @PerActivity
    //TODO provide interface instead of implementation
    fun provideLoginUseCase(authRepository: AuthRepository): LoginUseCase {
        return LoginUseCase(authRepository)
    }

    @Provides
    @PerActivity
    //TODO provide interface instead of implementation
    fun provideSearchUseCase(searchRepository: SearchRepository): SearchUseCase {
        return SearchUseCase(searchRepository)
    }



}
