package com.santimattius.basic.skeleton.core.networking


import okhttp3.Interceptor
import okhttp3.Response

class ExampleInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val url = request.url.newBuilder()
            .build()

        val newRequest = request.newBuilder()
            .url(url)
            .addHeader("Custom", "Say Hello")
            .build()
        return chain.proceed(newRequest)
    }
}