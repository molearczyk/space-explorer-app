package com.molearczyk.spaceexplorer.network.models

import okhttp3.HttpUrl

data class GalleryRecord(val previewImage: HttpUrl, val details: HttpUrl)