package com.molearczyk.spaceexplorer.ui.imagedetail

import android.text.SpannableStringBuilder
import android.util.Log
import androidx.core.text.HtmlCompat
import androidx.core.text.bold
import androidx.core.text.scale
import com.molearczyk.spaceexplorer.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.ui.BasePresenter
import com.molearczyk.spaceexplorer.ui.main.GalleryRecordEvent
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject

class ImageDetailPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository) : BasePresenter<ImageDetailsView>() {

    fun fetchImageDetail(event: GalleryRecordEvent) {
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

    fun resolveDescription(event: GalleryRecordEvent): SpannableStringBuilder =
            SpannableStringBuilder()
                    .scale(1.2f) {
                        bold {
                            append(event.title)
                        }
                    }
                    .append("\n")
                    .append(HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_COMPACT))

}