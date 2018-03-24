package com.dmytrod.githubsearch.model.api

import retrofit2.http.*
import rx.Observable

/**
 * Created by Dmytro Denysenko on 11.02.18.
 */
interface GithubService {

    @POST()
    fun login(@Url url: String, @Body loginBody: LoginBody): Observable<LoginResponse>

    @GET("/search/repositories")
    fun search(@Query("q") query: String, @Query("sort") sort: String, @Query("order") order: String, @Query("page") page: Int, @Query("per_page") perPage: Int) : Observable<Wrapper>
}