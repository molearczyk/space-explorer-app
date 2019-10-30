package com.molearczyk.spaceexplorer

import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.network.models.GalleryEntry
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.network.nasaendpoints.CollectionContainer
import com.molearczyk.spaceexplorer.network.nasaendpoints.ImagesNasaNetworkApi
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
            it.collection.items.isNotEmpty()
        }

    }

    @Test
    fun `image repository instantiates and converts all nasa models into app models correctly`() {
        val nasaImageNetworkApi = initNetworkApi()

        NasaImagesRepository(nasaImageNetworkApi)
                .fetchImages()
                .test()
                .awaitCount(1)
                .assertValue {
                    it.isNotEmpty()
                }

        Single.zip(NasaImagesRepository(nasaImageNetworkApi)
                .fetchImages(),
                nasaImageNetworkApi.search(),
                BiFunction { repositoryModels: List<GalleryEntry>, nasaModels: CollectionContainer ->
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
    fun `querying nasa api with a 'Moon' keyword and asking for 2nd page returns keyword and titles with Moon successfully`() {
        val nasaImageNetworkApi = initNetworkApi()

        nasaImageNetworkApi.search(keywords = "Moon", page = 2)
                .test()
                .awaitCount(1)
                .assertValue {
                    print(it)
                    it.collection.items.any {
                        it.data.any {
                            it.keywords?.contains("Moon") ?: false
                        }
                    } && it.collection.links?.find { it.prompt == "Previous" } != null
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

    @Test
    fun `querying nasa api for most popular images returns container of image links successfully`() {
        val nasaImageNetworkApi = initNetworkApi()
        nasaImageNetworkApi.fetchPopular()
                .test()
                .awaitCount(1)
                .assertValue {
                    true
                }
    }


    private fun initNetworkApi(): ImagesNasaNetworkApi {
        val networkModule = NetworkModule()
        val serverProperties: ServerProperties = networkModule.provideServerProperties()
        val retrofit: Retrofit = networkModule.provideRetrofit(serverProperties, networkModule.provideOkHttpClient())
        return networkModule.provideNasaImageNetworkApi(retrofit)
    }

}
