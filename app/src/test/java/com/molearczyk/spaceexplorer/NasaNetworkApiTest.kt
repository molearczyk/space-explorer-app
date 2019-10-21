package com.molearczyk.spaceexplorer

import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.network.models.GalleryRecord
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer
import com.molearczyk.spaceexplorer.network.nasa.ImagesNasaNetworkApi
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.junit.Test
import retrofit2.Retrofit

class NasaNetworkApiTest {

    @Test
    fun `nasa image search api is constructed correctly and parses models correctly`() {

        val nasaImageNetworkApi = initNetworkApi()

        nasaImageNetworkApi.search().test().awaitCount(1).assertValue {
            print(it)
            it.collection.items.isNotEmpty()
        }

    }

    @Test
    fun `image repository instantiates and converts all nasa models into app models correctly`() {
        val nasaImageNetworkApi = initNetworkApi()

        NasaImagesRepository(nasaImageNetworkApi)
                .fetchImages()
                .toList()
                .test()
                .awaitCount(1)
                .assertValue {
                    it.isNotEmpty()
                }

        Single.zip(NasaImagesRepository(nasaImageNetworkApi)
                .fetchImages()
                .toList(),
                nasaImageNetworkApi.search(),
                BiFunction { repositoryModels: MutableList<GalleryRecord>, nasaModels: CollectionContainer ->
                    repositoryModels.size == nasaModels.collection.items.size
                })
                .test()
                .awaitCount(1)
                .assertValue {
                    it
                }

    }

    @Test
    fun `querying nasa api with a 'Moon' keyword returns keyword and titles with Moon successfully`() {
        val nasaImageNetworkApi = initNetworkApi()

        nasaImageNetworkApi.search("Moon")
                .map { it.collection.items }
                .test()
                .awaitCount(1)
                .assertValue {
                    it.any {
                        it.data.any {
                            it.keywords?.contains("Moon") ?: false
                        }
                    } && it.any { it.data.any { "Moon" in it.title } }
                }


    }

    @Test
    fun `querying nasa api with 'Moon' and 'Mars' keywords returns keywords and titles with Moon and Mars successfully`() {
        val nasaImageNetworkApi = initNetworkApi()

        nasaImageNetworkApi.search("Moon Mars")
                .map { it.collection.items }
                .test()
                .awaitCount(1)
                .assertValue {
                    it.any {
                        it.data.any {
                            it.keywords?.contains("Moon") ?: false
                        }
                    } && it.any { it.data.any { "Moon" in it.title } }
                            && it.any {
                        it.data.any {
                            it.keywords?.contains("Mars") ?: false
                        }
                    } && it.any { it.data.any { "Mars" in it.title } }
                }


    }

    @Test
    fun `querying nasa api for images returns container of image links successfully`() {
        val nasaImageNetworkApi = initNetworkApi()
        nasaImageNetworkApi.search()
                .map { it.collection.items }
                .flatMap {
                    nasaImageNetworkApi.fetchImageLinkContainer(it[0].href.toHttpUrl())
                }
                .test()
                .awaitCount(1)
                .assertValue {
                    it.isNotEmpty()
                }
    }


    private fun initNetworkApi(): ImagesNasaNetworkApi {
        val networkModule = NetworkModule()
        val serverProperties: ServerProperties = networkModule.provideServerProperties()
        val retrofit: Retrofit = networkModule.provideRetrofit(serverProperties)
        return networkModule.provideNasaImageNetworkApi(retrofit)
    }

}
