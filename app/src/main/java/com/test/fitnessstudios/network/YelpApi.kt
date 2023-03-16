package com.test.fitnessstudios.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface YelpApi {
    /*
    * ?sort_by=best_match&limit=20
    *     @GET("/3/movie/now_playing")
    suspend fun getMovies(@QueryMap params:Map<String, String>) : Response<com.example.moviesample.model.Result>
    * */
    @GET("/v3/businesses/search")
    suspend fun getBusinesses(@QueryMap params: Map<String, String>): Response<com.test.fitnessstudios.model.Result>
}