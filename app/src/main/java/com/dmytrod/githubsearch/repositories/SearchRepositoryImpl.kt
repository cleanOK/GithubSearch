package com.dmytrod.githubsearch.repositories

import com.dmytrod.githubsearch.model.Repository
import com.dmytrod.githubsearch.model.api.GithubService
import com.dmytrod.githubsearch.screens.search.SearchContract
import rx.Observable

class SearchRepositoryImpl(private val mGithubService: GithubService) : SearchRepository {
    override fun findRepositories(query: String, page: Int): Observable<List<Repository>> {
        return mGithubService.search(query, SORT_VALUE, ORDER_VALUE, page, SearchContract.PAGE_SIZE / 2)
                .map {
                    it.items.map { Repository(it.id, it.name, it.stargazersCount, false) }
                }
    }

    companion object {
        const val SORT_VALUE = "stars"
        const val ORDER_VALUE = "stars"
    }
}