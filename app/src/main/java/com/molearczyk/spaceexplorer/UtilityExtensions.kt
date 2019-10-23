package com.molearczyk.spaceexplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.core.content.getSystemService
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

fun <T> lazyOnMainThread(initializer: () -> T): Lazy<T> =
        lazy(LazyThreadSafetyMode.NONE, initializer)

fun Throwable.isNetworkError(): Boolean = this is UnknownHostException

fun View.hideKeyboard() {
    this.context.getSystemService<InputMethodManager>()?.hideSoftInputFromWindow(windowToken, 0)
}