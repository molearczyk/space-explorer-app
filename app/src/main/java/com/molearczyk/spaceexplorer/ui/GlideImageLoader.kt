package com.molearczyk.spaceexplorer.ui

import android.app.Activity
import android.widget.ImageView
import com.bumptech.glide.Glide
import okhttp3.HttpUrl

class GlideImageLoader : ImageLoader {
    override fun loadCroppedImageInto(url: HttpUrl, target: ImageView) {
        Glide.with(target.context as Activity)
                .load(url.toString())
                .centerCrop()
                .into(target)
    }

    override fun loadCenteredImageInto(url: HttpUrl, target: ImageView) {
        Glide.with(target.context as Activity)
                .load(url.toString())
                .centerInside()
                .into(target)

    }

}