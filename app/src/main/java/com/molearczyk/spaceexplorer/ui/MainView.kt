package com.molearczyk.spaceexplorer.ui

import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.ui.main.GalleryEntryEvent

interface MainView {

    fun showNewImages(newImages: List<GalleryEntry>)

    fun appendImages(additionalImages: List<GalleryEntry>)

    fun navigateToFullscreen(event: GalleryEntryEvent)

    fun showInternetAccessError()

    fun showGenericError()

    fun showNoResultsWarning()

    fun hidePromptViews()
}