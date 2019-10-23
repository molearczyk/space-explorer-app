package com.molearczyk.spaceexplorer.ui.main

import android.text.Editable
import android.util.Log
import com.molearczyk.spaceexplorer.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.ui.BasePresenter
import com.molearczyk.spaceexplorer.ui.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository) : BasePresenter<MainView>() {

    fun onQuerySearch(query: Editable? = null) {
        subscriptions.add(nasaImagesRepository
                .fetchImages(query?.toString()?.takeIf(String::isNotBlank))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::handleQueryError))
    }

    fun onRetryClicked() {
        subscriptions.add(nasaImagesRepository
                .redoQuery()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::handleQueryError))

    }

    fun onNextPageRequested() {
        subscriptions.add(nasaImagesRepository
                .fetchNextPage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::appendImages) {
                    if (it.isNetworkError()) {
                        Log.w("MainPresenter", "Caught network error", it)
                        view.showInternetAccessError()
                    } else {
                        Log.e("MainPresenter", "Caught generic error", it)
                        view.showGenericError()
                    }
                })

    }

    private fun handleResult(images: List<GalleryEntry>) {
        if (images.isEmpty()) {
            view.showNoResultsWarning()
        } else {
            view.hidePromptViews()
            view.showNewImages(images)
        }
    }

    private fun handleQueryError(error: Throwable) {
        if (error.isNetworkError()) {
            Log.w("MainPresenter", "Caught network error", error)
            view.showInternetAccessError()
            view.showNewImages(emptyList())
        } else {
            Log.e("MainPresenter", "Caught generic error", error)
            view.showGenericError()
            view.showNewImages(emptyList())
        }

    }

}