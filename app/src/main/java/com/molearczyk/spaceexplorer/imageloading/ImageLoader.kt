package com.molearczyk.spaceexplorer.imageloading

import android.widget.ImageView
import okhttp3.HttpUrl

interface ImageLoader {

    fun loadCroppedImageInto(url: HttpUrl, target: ImageView)

    fun loadCenteredImageInto(url: HttpUrl, target: ImageView)

}

