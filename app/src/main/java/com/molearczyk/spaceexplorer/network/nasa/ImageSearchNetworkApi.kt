package com.molearczyk.spaceexplorer.network.nasa

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface ImageSearchNetworkApi {

    @GET("search?media_type=image")
    fun search(@Query("q") keywords: String? = null): Single<CollectionContainer>

}