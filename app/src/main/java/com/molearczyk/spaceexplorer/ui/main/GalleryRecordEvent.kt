package com.molearczyk.spaceexplorer.ui.main

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class GalleryRecordEvent(val nasaIdentifier: String, val title: String, val description: String) : Parcelable