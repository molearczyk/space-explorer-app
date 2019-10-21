package com.molearczyk.spaceexplorer.network

import com.molearczyk.spaceexplorer.network.models.GalleryRecord
import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer
import com.molearczyk.spaceexplorer.network.nasa.ImagesNasaNetworkApi
import io.reactivex.Flowable
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject


class NasaImagesRepository @Inject constructor(private val imageSearchApi: ImagesNasaNetworkApi) {

    fun fetchImages(queryKeywords: String? = null): Flowable<GalleryRecord> =
            imageSearchApi.search(queryKeywords)
                    .map(CollectionContainer::collection)
                    .flatMapPublisher { Flowable.fromIterable(it.items) }
                    .filter { it.links.any { it.rel == PreviewLink } }
                    .map { GalleryRecord(it.links.find { it.rel == PreviewLink }!!.href.toHttpUrl(), it.href.toHttpUrl(), it.data[0].title, it.data[0].description) }


    fun fetchImageAddress(linksSource: HttpUrl): Single<HttpUrl> = imageSearchApi.fetchImageLinkContainer(linksSource)
            .map(this@NasaImagesRepository::resolveImageUrl)
            .map { it.newBuilder().scheme("https").build() }


    private fun resolveImageUrl(urlList: List<String>): HttpUrl {
        return urlList.filter { !it.endsWith(".json") }
                .let { imageUrlList ->
                    require(imageUrlList.isNotEmpty()) { "No result" }
                    return (imageUrlList.find { it.contains("-large") }
                            ?: imageUrlList.find { it.contains("-orig") }
                            ?: imageUrlList.find { it.contains("-medium") }
                            ?: imageUrlList[0]).toHttpUrl()
                }
    }


    companion object NasaKeywords {
        const val PreviewLink = "preview"
    }

}

