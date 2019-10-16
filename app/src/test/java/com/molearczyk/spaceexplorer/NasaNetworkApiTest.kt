package com.molearczyk.spaceexplorer

import com.molearczyk.spaceexplorer.network.NasaImagesRepository
import com.molearczyk.spaceexplorer.network.NetworkModule
import com.molearczyk.spaceexplorer.network.models.GalleryRecord
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.network.nasa.CollectionContainer
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import org.junit.Test
import retrofit2.Retrofit

class NasaNetworkApiTest {

    @Test
    fun `nasa image search api is constructed correctly and parses models correctly`() {

        val networkModule = NetworkModule()
        val serverProperties: ServerProperties = networkModule.provideServerProperties()
        val retrofit: Retrofit = networkModule.provideRetrofit(serverProperties)
        val nasaImageNetworkApi = networkModule.provideNasaImageNetworkApi(retrofit)

        nasaImageNetworkApi.search().test().awaitCount(1).assertValue {
            it.collection.items.isNotEmpty()
        }

    }

    @Test
    fun `image repository instantiates and converts all nasa models into app models correctly`() {
        val networkModule = NetworkModule()
        val serverProperties: ServerProperties = networkModule.provideServerProperties()
        val retrofit: Retrofit = networkModule.provideRetrofit(serverProperties)
        val nasaImageNetworkApi = networkModule.provideNasaImageNetworkApi(retrofit)

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
                }
        )
                .test()
                .awaitCount(1)
                .assertValue {
                    it
                }

    }


}
