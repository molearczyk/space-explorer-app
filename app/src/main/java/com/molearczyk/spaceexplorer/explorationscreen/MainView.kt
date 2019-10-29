package com.molearczyk.spaceexplorer.explorationscreen

import com.molearczyk.spaceexplorer.explorationscreen.models.GalleryEntryEvent
import com.molearczyk.spaceexplorer.network.models.GalleryEntry

interface MainView {

    fun showNewImages(newImages: List<GalleryEntry>)

    fun appendImages(additionalImages: List<GalleryEntry>)

    fun navigateToFullscreen(event: GalleryEntryEvent)

    fun showInternetAccessError()

    fun showGenericError()

    fun onHintReloaded(keywords: String)
}