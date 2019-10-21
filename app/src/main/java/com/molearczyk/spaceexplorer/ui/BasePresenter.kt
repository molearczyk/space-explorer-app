package com.molearczyk.spaceexplorer.ui

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : Any> {

    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    protected lateinit var view: T

    open fun initView(source: T) {
        view = source
    }

    open fun onCleanup() {
        subscriptions.clear()
    }
}