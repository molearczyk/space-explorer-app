package com.molearczyk.spaceexplorer.detailedscreen

import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import androidx.core.text.HtmlCompat
import com.molearczyk.spaceexplorer.basics.BasePresenter
import com.molearczyk.spaceexplorer.basics.isNetworkError
import com.molearczyk.spaceexplorer.detailedscreen.models.Description
import com.molearczyk.spaceexplorer.explorationscreen.models.GalleryEntryEvent
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.schedulers.SchedulerProvider
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class ImageDetailPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository,
                                               private val schedulers: SchedulerProvider,
                                               private val event: GalleryEntryEvent,
                                               view: ImageDetailsView) : BasePresenter<ImageDetailsView>(view) {

    private var areSystemControlsVisible: Boolean = true

    private var isDescriptionExpanded = false

    private lateinit var description: Description

    fun fetchImageDetail() {
        subscriptions.add(nasaImagesRepository
                .fetchImageAddress(event.nasaIdentifier.toHttpUrl())
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
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


    fun resolveTitle(): CharSequence = event.title

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
