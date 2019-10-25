package com.molearczyk.spaceexplorer.ui.detail

import android.text.SpannableStringBuilder
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.core.text.bold
import androidx.core.text.scale
import com.molearczyk.spaceexplorer.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.ui.BasePresenter
import com.molearczyk.spaceexplorer.ui.main.GalleryEntryEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class ImageDetailPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository) : BasePresenter<ImageDetailsView>() {

    private lateinit var event: GalleryEntryEvent

    private var areSystemControlsVisible: Boolean = true

    private var isDescriptionExpanded = false

    private lateinit var description: Description

    fun init(event: GalleryEntryEvent) {
        this.event = event
    }

    fun fetchImageDetail() {
        subscriptions.add(nasaImagesRepository
                .fetchImageAddress(event.nasaIdentifier.toHttpUrl())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showImage) {
                    if (it.isNetworkError()) {
                        Log.w("MainActivity", "Caught network error", it)
                        view.showInternetAccessError()

                    } else {
                        Log.e("MainActivity", "Caught generic error", it)
                        view.showGenericError()
                    }
                })
    }


    fun resolveTitle(): CharSequence =
            SpannableStringBuilder()
                    .scale(1.4f) {
                        bold {
                            append(event.title)
                        }
                    }

    fun onFullImageClick() {
        if (areSystemControlsVisible) {
            view.hideSystemUi()
        } else {
            view.showSystemUi()
        }
        areSystemControlsVisible = !areSystemControlsVisible
    }

    fun retryOnClick() {
        fetchImageDetail()
    }

    fun onExpandClick() {
        isDescriptionExpanded = !isDescriptionExpanded
        if (isDescriptionExpanded) {
            view.transitionToFullDescription(description.longText)
        } else {
            view.transitionToShortDescription(description.shortText)
        }
    }

    fun resolveDescriptionSizing(destinationPaint: TextPaint, availableTextSpace: Float): Description =
            when {
                this::description.isInitialized -> description
                else -> {
                    val decodedDescription = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_COMPACT)
                    description = if (destinationPaint.measureText(decodedDescription.toString()) >= availableTextSpace) {
                        Description.Expandable(TextUtils.ellipsize(decodedDescription, destinationPaint, availableTextSpace, TextUtils.TruncateAt.END), decodedDescription)
                    } else {
                        Description.NonExpandable(decodedDescription)
                    }
                    description
                }
            }
}
