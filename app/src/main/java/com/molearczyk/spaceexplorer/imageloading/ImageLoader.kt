package com.molearczyk.spaceexplorer.imageloading

import android.widget.ImageView
import okhttp3.HttpUrl

interface ImageLoader {

    fun loadThumbnailImageInto(url: HttpUrl, target: ImageView)

    fun loadFullImageInto(url: HttpUrl, target: ImageView)

}

