package com.molearczyk.spaceexplorer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
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


fun Throwable.isNetworkError(): Boolean = this is UnknownHostException