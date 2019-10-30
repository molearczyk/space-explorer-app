package com.molearczyk.spaceexplorer.network.models

import com.molearczyk.spaceexplorer.network.nasaendpoints.CollectionContainer

data class ImageCacheRepresentation(val queryKeywords: String?, val page: Int?, val hasNextPage: Boolean, val result: CollectionContainer?)