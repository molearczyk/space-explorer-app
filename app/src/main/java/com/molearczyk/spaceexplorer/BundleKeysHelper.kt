package com.molearczyk.spaceexplorer

import android.content.Intent
import com.molearczyk.spaceexplorer.ui.main.GalleryEntryEvent

fun Intent.putGalleryEvent(parameter: GalleryEntryEvent): Intent = putExtra(
        "com.molearczyk.spaceexplorer.GalleryEntry", parameter
)

fun Intent.getGalleryEvent(): GalleryEntryEvent =
        getParcelableExtra("com.molearczyk.spaceexplorer.GalleryEntry")!!
