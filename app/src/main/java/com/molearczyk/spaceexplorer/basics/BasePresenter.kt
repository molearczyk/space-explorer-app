package com.molearczyk.spaceexplorer.basics

import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : Any>(baseView: T) {

    protected val subscriptions: CompositeDisposable = CompositeDisposable()

    protected var view: T = baseView

    open fun onCleanup() {
        subscriptions.clear()
    }
}