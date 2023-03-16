package com.test.fitnessstudios.network


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
/*
OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
      @Override
      public Response intercept(Chain chain) throws IOException {
        Request newRequest  = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer " + token)
            .build();
        return chain.proceed(newRequest);
      }
* */
    val client = OkHttpClient.Builder().addInterceptor {
        val newRequest = it.request().newBuilder()
            .addHeader("Authorization", "Bearer " + yelpAPIKey)
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