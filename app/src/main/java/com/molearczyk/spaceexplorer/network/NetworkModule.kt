package com.molearczyk.spaceexplorer.network

import android.util.Log
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.molearczyk.spaceexplorer.BuildConfig
import com.molearczyk.spaceexplorer.network.models.ServerProperties
import com.molearczyk.spaceexplorer.network.nasaendpoints.ImagesNasaNetworkApi
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.jackson.JacksonConverterFactory

@Module
class NetworkModule {

    @Provides
    fun provideServerProperties(): ServerProperties = ServerProperties("https://images-api.nasa.gov".toHttpUrl())

    @Provides
    fun provideOkHttpClient(): OkHttpClient = if (BuildConfig.DEBUG) {
        OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
                    Log.d("NetworkCall", it)
                }).setLevel(HttpLoggingInterceptor.Level.BODY)
        ).build()
    } else {
        OkHttpClient.Builder().build()
    }

    @Provides
    fun provideRetrofit(properties: ServerProperties, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .client(okHttpClient)
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

