package com.dmytrod.githubsearch.screens.search

import com.dmytrod.githubsearch.model.Repository
import com.dmytrod.githubsearch.repositories.SearchRepository
import com.dmytrod.githubsearch.screens.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Dmytro Denysenko on 12.02.18.
 */
class SearchUseCase(private val searchRepository: SearchRepository) : UseCase<List<Repository>>(Schedulers.io(), AndroidSchedulers.mainThread()) {
    var page: Int = 0
    var query: String? = null

    override fun buildUseCaseObservable(): Observable<List<Repository>> {
        if (page < 1 || query == null) {
            throw IllegalArgumentException("page and query should be set")
        }
        val firstPage = page * 2 - 1
        val secondPage = page * 2
        if (query!!.isEmpty()) {
            return Observable.empty()
        }
        return Observable.zip(searchRepository.findRepositories(query!!, firstPage)
                .observeOn(Schedulers.io()),
                searchRepository.findRepositories(query!!, secondPage)
                        .observeOn(Schedulers.io()))
        { firstPageResult, secondPageResult -> firstPageResult + secondPageResult }
    }
}