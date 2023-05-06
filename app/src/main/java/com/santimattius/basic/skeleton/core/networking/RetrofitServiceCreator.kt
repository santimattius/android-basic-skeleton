package com.santimattius.basic.skeleton.core.networking

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class RetrofitServiceCreator(val baseUrl: String) {

    private val client = OkHttpClient().newBuilder()
        .addInterceptor(ExampleInterceptor())
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    inline fun <reified T> create() = retrofit.create<T>()
}