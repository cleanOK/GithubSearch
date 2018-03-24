package com.dmytrod.githubsearch.screens

interface BaseContract {
    interface Presenter<T> {
        fun setView(view : T)
        fun destroy()
    }
}