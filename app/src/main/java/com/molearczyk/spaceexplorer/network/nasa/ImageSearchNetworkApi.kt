package com.molearczyk.spaceexplorer.network.nasa

import io.reactivex.Single
import retrofit2.http.GET


interface ImageSearchNetworkApi {

    @GET("search?media_type=image")
    fun search(): Single<CollectionContainer>

}