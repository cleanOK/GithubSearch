package com.dmytrod.githubsearch.screens.search

import com.dmytrod.githubsearch.model.api.LoginResponse
import com.dmytrod.githubsearch.repositories.AuthRepository
import com.dmytrod.githubsearch.screens.UseCase
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

/**
 * Created by Dmytro Denysenko on 11.02.18.
 */
class LoginUseCase(private val authRepository: AuthRepository)
    : UseCase<Boolean>(Schedulers.io(), AndroidSchedulers.mainThread()) {

    var code: String? = null
    override fun buildUseCaseObservable(): Observable<Boolean> = authRepository.login(code!!)
            .onErrorResumeNext {
                //TODO better handling, like throwing custom exception
                Observable.just(LoginResponse(""))
            }
            .flatMap {
                if (it.accessToken.isEmpty()) {
                    Observable.just(false)
                } else {
                    authRepository.storeToken(it.accessToken)
                }
            }
}