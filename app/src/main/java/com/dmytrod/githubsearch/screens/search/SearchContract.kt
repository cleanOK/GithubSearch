package com.dmytrod.githubsearch.screens.search

import android.net.Uri
import com.dmytrod.githubsearch.model.Repository
import com.dmytrod.githubsearch.screens.BaseContract

/**
 * Created by Dmytro Denysenko on 10.02.18.
 */
class SearchContract {
    interface View {
        fun showLoggedIn(result: Boolean)
        fun showError()
        fun showResults(results: List<Repository>)

    }

    interface Presenter : BaseContract.Presenter<View> {
        fun processIntentData(uri: Uri)
        fun checkAuthorization()
        fun performSearch(query: String)
        fun loadPage(page: Int)
        fun cancelSearch()
    }

    companion object {
        const val PAGE_SIZE = 30
    }
}