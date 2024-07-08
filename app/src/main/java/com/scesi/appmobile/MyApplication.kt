package com.scesi.appmobile

import android.app.Application
import com.scesi.appmobile.data.local.AppDatabase
import com.scesi.appmobile.data.network.ApiService
import com.scesi.appmobile.data.repository.MovieRepository
import com.scesi.appmobile.utils.Constantes
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApplication : Application() {

    val database by lazy { AppDatabase.getDatabase(this) }
    val apiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    val repository by lazy { MovieRepository(database.movieDao(), apiService) }
}
