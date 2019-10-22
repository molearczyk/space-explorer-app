package com.molearczyk.spaceexplorer.ui

import android.widget.ImageView
import okhttp3.HttpUrl

interface ImageLoader {

    fun loadCroppedImageInto(url: HttpUrl, target: ImageView)

    fun loadCenteredImageInto(url: HttpUrl, target: ImageView)

}

