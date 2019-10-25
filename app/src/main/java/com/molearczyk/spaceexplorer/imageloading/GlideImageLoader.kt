package com.molearczyk.spaceexplorer.imageloading

import android.app.Activity
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.molearczyk.spaceexplorer.R
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
                .placeholder(CircularProgressDrawable(target.context).apply {
                    val resources = target.context.resources
                    strokeWidth = resources.getDimension(R.dimen.in_progress_stroke_width)
                    centerRadius = resources.getDimension(R.dimen.in_progress_center_radius)
                    setColorSchemeColors(ContextCompat.getColor(target.context, R.color.colorAccent))
                    start()
                })
                .centerInside()
                .into(target)

    }

}