package com.molearczyk.spaceexplorer.network

import com.molearczyk.spaceexplorer.network.models.GalleryRecord
import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer
import com.molearczyk.spaceexplorer.network.nasa.ImageSearchNetworkApi
import io.reactivex.Flowable
import okhttp3.HttpUrl.Companion.toHttpUrl

class NasaImagesRepository(private val imageSearchApi: ImageSearchNetworkApi) {

    fun fetchImages(): Flowable<GalleryRecord> {

        return imageSearchApi.search()
                .map(CollectionContainer::collection)
                .flatMapPublisher { Flowable.fromIterable(it.items) }
                .filter { it.links.any { it.rel == PreviewLink } }
                .map { GalleryRecord(it.links.find { it.rel == PreviewLink }!!.href.toHttpUrl(), it.href.toHttpUrl()) }
    }


    companion object NasaKeywords {
        const val PreviewLink = "preview"
    }

}

