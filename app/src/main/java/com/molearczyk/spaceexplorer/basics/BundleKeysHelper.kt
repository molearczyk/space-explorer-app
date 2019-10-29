package com.molearczyk.spaceexplorer.basics

import android.content.Intent
import com.molearczyk.spaceexplorer.explorationscreen.models.GalleryEntryEvent

fun Intent.putGalleryEvent(parameter: GalleryEntryEvent): Intent = putExtra(
        "com.molearczyk.spaceexplorer.GalleryEntry", parameter
)

fun Intent.getGalleryEvent(): GalleryEntryEvent =
        getParcelableExtra("com.molearczyk.spaceexplorer.GalleryEntry")!!
