package com.test.fitnessstudios.network


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val client: OkHttpClient = OkHttpClient.Builder().addInterceptor {
        val newRequest = it.request().newBuilder()
            .addHeader("Authorization", "Bearer $yelpAPIKey")
            .build()
        it.proceed(newRequest)
    }.build()

    private const val baseUrl = "https://api.yelp.com"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}