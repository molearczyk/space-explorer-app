package com.molearczyk.spaceexplorer.network

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.network.nasa.ImagesNasaNetworkApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideServerProperties(): ServerProperties = ServerProperties("https://images-api.nasa.gov".toHttpUrl())

    @Provides
    fun provideRetrofit(properties: ServerProperties): Retrofit {
        return Retrofit.Builder()
                .client(OkHttpClient.Builder().build())
                .baseUrl(properties.base)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper())).build()
    }

    @Provides
    @Reusable
    fun provideNasaImageNetworkApi(
            retrofit: Retrofit
    ): ImagesNasaNetworkApi {
        return retrofit.create(ImagesNasaNetworkApi::class.java)
    }

}

