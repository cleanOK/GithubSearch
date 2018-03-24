package com.dmytrod.githubsearch.screens.search

import android.net.Uri
import android.util.Log
import com.dmytrod.githubsearch.model.Repository
import com.dmytrod.githubsearch.repositories.AuthRepositoryImpl
import com.dmytrod.githubsearch.screens.BasePresenter
import rx.Subscriber

class SearchPresenter(private val mLoginUseCase: LoginUseCase, private val mSearchUseCase: SearchUseCase) : BasePresenter<SearchContract.View>(), SearchContract.Presenter {

    private val LOG_TAG = "SearchPresenter"

    override fun loadPage(page: Int) {
        mSearchUseCase.unsubscribe()
        mSearchUseCase.page = page
        mSearchUseCase.execute(SearchSubscriber())
    }

    override fun checkAuthorization() {

    }

    override fun performSearch(query: String) {
        mSearchUseCase.unsubscribe()
        mSearchUseCase.page = 1
        mSearchUseCase.query = query
        mSearchUseCase.execute(SearchSubscriber())
    }

    override fun cancelSearch() {
        mSearchUseCase.unsubscribe()
    }

    override fun processIntentData(uri: Uri) {
        val code = uri.getQueryParameter(AuthRepositoryImpl.CODE_KEY)
        val state = uri.getQueryParameter(AuthRepositoryImpl.STATE_KEY)
        if (state == AuthRepositoryImpl.STATE_VALUE) {
            mLoginUseCase.code = code
            mLoginUseCase.execute(object : Subscriber<Boolean>() {
                override fun onNext(result: Boolean?) {
                    when (result) {
                        null -> getView()?.showLoggedIn(false)
                        else -> getView()?.showLoggedIn(result)
                    }
                }

                override fun onCompleted() = Unit

                override fun onError(e: Throwable?) {
                    if (e != null) {
                        handleError(e)
                    }
                }
            })
        }
    }

    private fun handleError(throwable: Throwable) {
        //TODO add proper error handling
        Log.e(LOG_TAG, "Login failed", throwable)
        getView()?.showError()
    }

    private inner class SearchSubscriber : Subscriber<List<Repository>>() {
        override fun onNext(results: List<Repository>?) {
            if (results != null) {
                getView()?.showResults(results)
            }
        }

        override fun onCompleted() = Unit

        override fun onError(e: Throwable?) {
            //TODO handle no internet case
            getView()?.showError()
        }
    }
}