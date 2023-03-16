package com.test.fitnessstudios.network

import com.test.fitnessstudios.model.NavigationResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface DirectionsApi {
    @GET("/maps/api/directions/json")
    suspend fun getRoute(@QueryMap params:Map<String, String>) : Response<NavigationResult>
}