package com.molearczyk.spaceexplorer.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryEntryEvent(val nasaIdentifier: String, val title: String, val description: String, val layoutPosition: Int) : Parcelable