package com.molearczyk.spaceexplorer.ui.main

import android.text.Editable
import android.util.Log
import com.molearczyk.spaceexplorer.isNetworkError
import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.ui.BasePresenter
import com.molearczyk.spaceexplorer.ui.MainView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainPresenter @Inject constructor(private val nasaImagesRepository: NasaImagesRepository) : BasePresenter<MainView>() {

    fun onQuerySearch(query: Editable? = null) {
        subscriptions.add(nasaImagesRepository
                .fetchImages(query?.toString()?.takeIf(String::isNotBlank))
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(view::showImages) {
                    if (it.isNetworkError()) {
                        Log.w("MainActivity", "Caught network error", it)
                        view.showInternetAccessError()
                    } else {
                        Log.e("MainActivity", "Caught generic error", it)
                        view.showGenericError()
                    }
                })

    }

    fun onRetryClicked() {}


}