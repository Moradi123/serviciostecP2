package com.example.serviciostec.model.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    // Si uso emulador: 10.0.2.2
    // Si uso celular real por USB:  la IP PC (ej: 192.168.1.X)
    private const val BASE_URL = "http://10.0.2.2:8081/"

    val service: ProductoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductoApiService::class.java)
    }
}