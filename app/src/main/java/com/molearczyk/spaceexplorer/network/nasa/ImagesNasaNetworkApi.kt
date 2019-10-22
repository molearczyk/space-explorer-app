package com.molearczyk.spaceexplorer.network.nasa

import io.reactivex.Single
import okhttp3.HttpUrl
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url


interface ImagesNasaNetworkApi {

    @GET("search?media_type=image")
    fun search(@Query("q") keywords: String? = null): Single<CollectionContainer>

    @GET
    fun fetchImageLinkContainer(@Url fullPath: HttpUrl): Single<List<String>>

}