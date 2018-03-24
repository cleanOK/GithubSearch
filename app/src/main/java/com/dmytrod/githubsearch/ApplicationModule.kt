package com.dmytrod.githubsearch

import android.content.Context
import android.content.SharedPreferences
import com.dmytrod.githubsearch.model.api.GithubService
import com.dmytrod.githubsearch.repositories.AuthRepositoryImpl
import com.dmytrod.githubsearch.repositories.AuthRepository
import com.dmytrod.githubsearch.repositories.SearchRepository
import com.dmytrod.githubsearch.repositories.SearchRepositoryImpl
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
open class ApplicationModule(private val mContext: Context) {

    @Provides
    @Singleton
    fun provideGithubService(): GithubService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val headerInterceptor = Interceptor {
            val original = it.request()
            val request = original.newBuilder()
                    .addHeader("Accept", "application/json")
                    .method(original.method(), original.body())
                    .build()
            it.proceed(request)
        }
        val okHttpBuilder = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .addInterceptor(headerInterceptor)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpBuilder.build())
                .build()
        return retrofit.create(GithubService::class.java)
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences =
            mContext.getSharedPreferences(mContext.getString(R.string.shared_pref_file), Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideAuthRepository(githubService: GithubService, sharedPreferences: SharedPreferences): AuthRepository =
            AuthRepositoryImpl(githubService, sharedPreferences)

    @Provides
    @Singleton
    fun provideSearchRepository(githubService: GithubService): SearchRepository = SearchRepositoryImpl(githubService)
}