package com.scesi.appmobile.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.Response

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val queryInterceptor = QueryInterceptor("BuildConfig.API_KEY")

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(queryInterceptor)
        .build()
    class QueryInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()

            val requestWithApiKey = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build()

            return chain.proceed(requestWithApiKey)
        }
    }
}
