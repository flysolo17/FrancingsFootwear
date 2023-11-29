package com.ketchupzz.francingsfootwear.config

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton



object ApiInstance {
    private const val BASE_URL = "http://192.168.100.18/alphaware/"

    val api: Retrofit by lazy {
        createRetrofitInstance()
    }

    private fun createRetrofitInstance(): Retrofit {
        try {
            val gson = GsonBuilder().setLenient().create()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        } catch (e: Exception) {
            // Log the exception or handle it appropriately
            throw RuntimeException("Retrofit initialization error", e)
        }
    }
}