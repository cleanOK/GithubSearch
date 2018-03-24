package com.dmytrod.githubsearch.repositories

import com.dmytrod.githubsearch.model.Repository
import rx.Observable

/**
 * Created by Dmytro Denysenko on 12.02.18.
 */
interface SearchRepository {
    fun findRepositories(query: String, page: Int) : Observable<List<Repository>>
}