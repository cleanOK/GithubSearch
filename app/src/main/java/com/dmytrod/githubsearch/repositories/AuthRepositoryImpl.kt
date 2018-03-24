package com.dmytrod.githubsearch.repositories

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.dmytrod.githubsearch.model.api.GithubService
import com.dmytrod.githubsearch.model.api.LoginBody
import com.dmytrod.githubsearch.model.api.LoginResponse
import rx.Observable

class AuthRepositoryImpl(private val mGithubService: GithubService, private val sharedPreferences: SharedPreferences) : AuthRepository {


    @SuppressLint("ApplySharedPref")
    override fun storeToken(accessToken: String): Observable<Boolean> {
        return Observable.fromCallable(sharedPreferences.edit().putString(ACCESS_TOKEN, accessToken)::commit)
    }

    override fun login(code: String): Observable<LoginResponse> {
        return mGithubService.login("https://github.com/login/oauth/access_token", LoginBody(CLIENT_ID, CLIENT_SECRET, code))
    }

    companion object {
        //TODO move to secure storage
        //TODO move hardcode
        private const val ACCESS_TOKEN = "TOKEN_KEY"
        const val CLIENT_ID = "44ff4e8254559f207a53"
        const val CLIENT_SECRET = "a2f1a7ec730e0695ddc2277869ee357505bf859d"
        const val STATE_KEY = "state"
        const val STATE_VALUE = "frustration"
        const val CODE_KEY = "code"
    }
}