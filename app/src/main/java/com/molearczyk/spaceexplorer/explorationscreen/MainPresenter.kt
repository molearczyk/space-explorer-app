package com.molearczyk.spaceexplorer.explorationscreen

import android.text.Editable
import android.util.Log
import com.molearczyk.spaceexplorer.basics.BasePresenter
import com.molearczyk.spaceexplorer.basics.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.querytracking.Query
import com.molearczyk.spaceexplorer.querytracking.QueryTracker
import com.molearczyk.spaceexplorer.schedulers.SchedulerProvider
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class MainPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository,
                                        private val queryTracker: QueryTracker,
                                        private val schedulers: SchedulerProvider, mainView: MainView) : BasePresenter<MainView>(mainView) {

    fun onUserInputCleared() {
        subscriptions.add(nasaImagesRepository
                .fetchMostPopularImages()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResult, this::handleQueryError))
    }

    fun onInitializeData() {
        subscriptions.add(
                queryTracker.checkLatestQuery()
                        .doOnSuccess {
                            view.onHintReloaded(it.keywords!!)
                        }
                        .flatMapSingleElement {
                            nasaImagesRepository
                                    .fetchImages(it.keywords?.takeIf(String::isNotBlank))
                                    .subscribeOn(Schedulers.io())
                        }
                        .switchIfEmpty(nasaImagesRepository.fetchMostPopularImages()
                                .subscribeOn(Schedulers.io()))
                        .observeOn(schedulers.mainThread())
                        .subscribe(this::handleResult, this::handleQueryError))
    }

    fun onQuerySearch(query: Editable? = null) {
        subscriptions.add(
                queryTracker.onTrackQueryInvoked(Single.fromCallable { Query(query.toString()) })
                        .flatMap {
                            nasaImagesRepository.fetchImages(it.keywords?.takeIf(String::isNotBlank))
                        }
                        .subscribeOn(schedulers.io())
                        .observeOn(schedulers.mainThread())
                        .subscribe(this::handleResult, this::handleQueryError))
    }

    fun onRetryClicked() {
        subscriptions.add(nasaImagesRepository
                .redoQuery()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe(this::handleResult, this::handleQueryError))

    }

    fun onNextPageRequested() {
        subscriptions.add(nasaImagesRepository
                .fetchNextPage()
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.mainThread())
                .subscribe(view::appendImages, this::handleQueryError))

    }

    private fun handleResult(images: List<GalleryEntry>) {
        view.showNewImages(images)
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
