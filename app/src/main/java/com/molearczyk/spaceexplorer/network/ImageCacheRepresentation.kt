package com.molearczyk.spaceexplorer.network

import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer

data class ImageCacheRepresentation(val queryKeywords: String?, val page: Int?, val hasNextPage: Boolean, val result: CollectionContainer?)