package com.scesi.appmobile.network

import com.scesi.appmobile.core.Constantes
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val queryInterceptor = QueryInterceptor("7ad52d9647575a91111e3600fa6cc563")

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(queryInterceptor)
        .build()
    // Create Retrofit instance
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constantes.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // Create the ApiService
    val apiService: ApiService = retrofit.create(ApiService::class.java)



    class QueryInterceptor(private val apiKey: String) : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalUrl = originalRequest.url

            // Add the API key to the original URL
            val urlWithApiKey = originalUrl.newBuilder()
                .addQueryParameter("api_key", apiKey)
                .build()

            // Build a new request with the modified URL
            val requestWithApiKey = originalRequest.newBuilder()
                .url(urlWithApiKey)
                .build()

            return chain.proceed(requestWithApiKey)
        }
    }
}