package com.molearczyk.spaceexplorer.imageloading

import android.app.Activity
import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.molearczyk.spaceexplorer.R
import okhttp3.HttpUrl

class GlideImageLoader : ImageLoader {

    override fun loadThumbnailImageInto(url: HttpUrl, target: ImageView) {
        Glide.with(target.context as Activity)
                .load(url.toString())
                .into(target)
    }

    override fun loadFullImageInto(url: HttpUrl, target: ImageView) {
        Glide.with(target.context as Activity)
                .load(url.toString())
                .placeholder(createProgressDrawable(target.context))
                .centerInside()
                .into(target)
    }

    private fun createProgressDrawable(context: Context): CircularProgressDrawable =
            CircularProgressDrawable(context).apply {
                val resources = context.resources
                strokeWidth = resources.getDimension(R.dimen.in_progress_stroke_width)
                centerRadius = resources.getDimension(R.dimen.in_progress_center_radius)
                setColorSchemeColors(ContextCompat.getColor(context, R.color.colorPrimary))
                start()
            }

}