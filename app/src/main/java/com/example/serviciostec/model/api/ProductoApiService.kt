package com.example.serviciostec.model.api

import com.example.serviciostec.model.data.entities.ProductoEntity
import retrofit2.http.GET

interface ProductoApiService {
    @GET("/api/productos")
    suspend fun obtenerProductos(): List<ProductoEntity>
}