package com.molearczyk.spaceexplorer.network

import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer
import com.molearczyk.spaceexplorer.network.nasa.ImagesNasaNetworkApi
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import javax.inject.Inject


class NasaImagesRepository @Inject constructor(private val imageSearchApi: ImagesNasaNetworkApi) {


    private var memoryCache: ImageCacheRepresentation = EmptyCache

    private var lastRequest: RequestRepresentation? = null

    fun fetchImages(queryKeywords: String? = null, page: Int? = null): Single<List<GalleryEntry>> {
        lastRequest = RequestRepresentation(queryKeywords, page)
        return Maybe.concat<CollectionContainer>(
                Single.fromCallable<ImageCacheRepresentation> {
                    memoryCache
                }
                        .filter { it !== EmptyCache && it.queryKeywords == queryKeywords && it.page == page && it.result != null }
                        .map(ImageCacheRepresentation::result),
                imageSearchApi.search(queryKeywords, page).toMaybe()
                        .doAfterSuccess {
                            val nextPage = it.collection.links?.find { it.prompt == "Next" }
                            memoryCache = ImageCacheRepresentation(queryKeywords, page, nextPage != null, it)
                        })
                .firstElement()
                .map(CollectionContainer::collection)
                .flatMapPublisher { Flowable.fromIterable(it.items) }
                .filter { it.links.any { it.rel == PreviewLink } }
                .map {
                    GalleryEntry(it.links.find { it.rel == PreviewLink }!!.href.toHttpUrl(), it.href.toHttpUrl(), it.data[0].title, it.data[0].description
                            ?: "")
                }
                .toList()
    }

    fun fetchNextPage(): Maybe<List<GalleryEntry>> {
        return if (memoryCache.hasNextPage) {
            val nextPage: Int = memoryCache.page?.plus(1) ?: 2
            lastRequest = RequestRepresentation(memoryCache.queryKeywords, nextPage)
            fetchImages(memoryCache.queryKeywords, nextPage).toMaybe()
        } else {
            Maybe.empty()
        }
    }

    fun redoQuery(): Single<List<GalleryEntry>> {
        return fetchImages(lastRequest?.queryKeywords, lastRequest?.page)
    }

    fun fetchImageAddress(linksSource: HttpUrl): Single<HttpUrl> =
            imageSearchApi.fetchImageLinkContainer(linksSource)
                    .map(this@NasaImagesRepository::resolveImageUrl)
                    .map { it.newBuilder().scheme("https").build() }


    private fun resolveImageUrl(urlList: List<String>): HttpUrl =
            urlList.filter { !it.endsWith(".json") }
                    .let { imageUrlList: List<String> ->
                        require(imageUrlList.isNotEmpty()) { "No result" }
                        (imageUrlList.find { it.contains("-large") }
                                ?: imageUrlList.find { it.contains("-orig") }
                                ?: imageUrlList.find { it.contains("-medium") }
                                ?: imageUrlList[0]).toHttpUrl()
                    }


    companion object NasaKeywords {
        const val PreviewLink = "preview"
        val EmptyCache = ImageCacheRepresentation(null, null, false, null)
    }

}

