package com.molearczyk.spaceexplorer.ui.main

import com.molearczyk.spaceexplorer.network.models.GalleryEntry

interface MainView {

    fun showNewImages(newImages: List<GalleryEntry>)

    fun appendImages(additionalImages: List<GalleryEntry>)

    fun navigateToFullscreen(event: GalleryEntryEvent)

    fun showInternetAccessError()

    fun showGenericError()

    fun showNoResultsWarning()

    fun hidePromptViews()

    fun onHintReloaded(keywords: String)
}