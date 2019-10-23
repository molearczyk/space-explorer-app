package com.molearczyk.spaceexplorer.ui

import com.molearczyk.spaceexplorer.network.models.GalleryEntry

interface MainView {

    fun showNewImages(newImages: List<GalleryEntry>)

    fun appendImages(additionalImages: List<GalleryEntry>)

    fun navigateToFullscreen(event: GalleryEntry)

    fun showInternetAccessError()

    fun showGenericError()

    fun showNoResultsWarning()

    fun hidePromptViews()
}