package com.molearczyk.spaceexplorer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(
        @LayoutRes
        layout: Int
) = LayoutInflater.from(this.context).inflate(layout, this, false)!!
