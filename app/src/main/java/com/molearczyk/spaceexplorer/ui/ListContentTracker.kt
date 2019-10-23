package com.molearczyk.spaceexplorer.ui

import com.molearczyk.spaceexplorer.network.models.GalleryEntry

class ListContentTracker(private val updateRequiredListener: GalleryNextPageListener, imagesPerRow: Int) {

    private val entries: MutableList<GalleryEntry> = mutableListOf()
    private val loadingThreshold: Int = imagesPerRow * 2
    private var latestAccessIndex: Int = Int.MIN_VALUE
    private var deferUpdates: Boolean = false

    val size: Int
        get() = entries.size

    fun getItem(position: Int): GalleryEntry {
        latestAccessIndex = position
        notifyOfAccess()
        return entries[position]
    }

    private fun notifyOfAccess() {
        if (!deferUpdates && latestAccessIndex >= size - loadingThreshold) {
            deferUpdates = true
            updateRequiredListener()
        }

    }

    fun appendEntries(newEntries: List<GalleryEntry>) {
        entries.addAll(newEntries)
        deferUpdates = false
    }

    fun setEntries(newEntries: List<GalleryEntry>) {
        entries.clear()
        entries.addAll(newEntries)
        deferUpdates = false
    }

}