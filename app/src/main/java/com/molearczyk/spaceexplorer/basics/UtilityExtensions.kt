package com.molearczyk.spaceexplorer.basics

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.content.getSystemService
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.net.UnknownHostException

fun ViewGroup.inflate(
        @LayoutRes
        layout: Int
) = LayoutInflater.from(this.context).inflate(layout, this, false)!!


fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun <T> lazyOnMainThread(initializer: () -> T): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE, initializer)

fun Throwable.isNetworkError(): Boolean = this is UnknownHostException

fun View.hideKeyboard() {
    this.context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}

operator fun CompositeDisposable.plusAssign(subscription: Disposable) {
    this.add(subscription)
}

inline fun Disposable.subscribeUsing(subscriptions: CompositeDisposable) {
    subscriptions += this
}
