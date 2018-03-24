package com.dmytrod.githubsearch.repositories

import com.dmytrod.githubsearch.model.api.LoginResponse
import rx.Observable

interface AuthRepository {
    fun login(code: String): Observable<LoginResponse>
    fun storeToken(accessToken: String): Observable<Boolean>
}