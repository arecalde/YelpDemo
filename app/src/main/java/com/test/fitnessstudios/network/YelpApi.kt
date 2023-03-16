package com.test.fitnessstudios.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YelpApi {

    @GET("/v3/businesses/search")
    suspend fun getBusinesses(@QueryMap params: Map<String, String>): Response<com.test.fitnessstudios.model.Result>
}