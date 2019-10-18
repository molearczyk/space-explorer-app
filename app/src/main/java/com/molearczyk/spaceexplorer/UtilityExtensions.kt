package com.molearczyk.spaceexplorer

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import java.net.UnknownHostException

fun ViewGroup.inflate(
        @LayoutRes
        layout: Int
) = LayoutInflater.from(this.context).inflate(layout, this, false)!!


inline val FragmentActivity.isLandscapeOrientation: Boolean
        get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun View.show() {
        this.visibility = View.VISIBLE
}

fun View.gone() {
        this.visibility = View.GONE
}


fun Throwable.isNetworkError(): Boolean = this is UnknownHostException