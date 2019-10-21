package com.molearczyk.spaceexplorer.ui

import com.molearczyk.spaceexplorer.network.models.GalleryRecord

interface MainView {

    fun showImages(newImages: List<GalleryRecord>)

    fun navigateToFullscreen(event: GalleryRecord)

    fun showInternetAccessError()

    fun showGenericError()
}