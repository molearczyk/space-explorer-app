package com.molearczyk.spaceexplorer.network.models

import okhttp3.HttpUrl

data class GalleryEntry(val previewImage: HttpUrl, val details: HttpUrl, val title: String, val description: String)