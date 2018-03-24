package com.dmytrod.githubsearch.screens

/**
 * Created by Dmytro Denysenko on 23.01.18.
 */
abstract class BasePresenter<T> : BaseContract.Presenter<T> {
    private var mView: T? = null

    override fun setView(view: T) {
        mView = view
    }

    override fun destroy() {
        mView = null
    }

    protected fun getView() = mView
}