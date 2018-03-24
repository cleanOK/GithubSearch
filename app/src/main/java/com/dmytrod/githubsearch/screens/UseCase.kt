package com.dmytrod.githubsearch.screens

import rx.Observable
import rx.Scheduler
import rx.Subscriber
import rx.Subscription
import rx.subscriptions.Subscriptions


/**
 * Created by Dmytro Denysenko on 11.02.18.
 */
public abstract class UseCase<T>(val mThreadExecutor: Scheduler, val mPostExecutionThread: Scheduler) {

    private var subscription = Subscriptions.empty()

    protected fun getThreadExecutor(): Scheduler {
        return mThreadExecutor
    }

    protected fun getPostExecutionThread(): Scheduler {
        return mPostExecutionThread
    }

    /**
     * Builds an [rx.Observable] which will be used when executing the current [UseCase].
     */
    protected abstract fun buildUseCaseObservable(): Observable<T>

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build with [.buildUseCaseObservable].
     */
    fun execute(useCaseSubscriber: Subscriber<T>) {
        unSubscribeFromCurrent(subscription)
        subscription = setupObservable()
                .subscribe(useCaseSubscriber)
    }

    protected fun unSubscribeFromCurrent(subscription: Subscription?) {
        if (subscription != null) {
            unsubscribe()
        }
    }

    /**
     * Unsubscribes from current [rx.Subscription].
     */
    fun unsubscribe() {
        if (!subscription.isUnsubscribed) {
            subscription.unsubscribe()
        }
    }

    fun setupObservable(): Observable<T> {
        return buildUseCaseObservable()
                .subscribeOn(mThreadExecutor)
                .observeOn(mPostExecutionThread)
    }
}