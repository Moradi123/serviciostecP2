package com.example.serviciostec.model.data.repository

import com.example.serviciostec.model.api.RetrofitClient
import com.example.serviciostec.model.data.dao.ProductoDao
import com.example.serviciostec.model.data.entities.ProductoEntity
import kotlinx.coroutines.flow.Flow

class ProductoRepository(private val productoDao: ProductoDao) {

    val productos: Flow<List<ProductoEntity>> = productoDao.getAllProductos()

    suspend fun recargarProductosDesdeApi() {
        try {
            val listaRemota = RetrofitClient.service.obtenerProductos()

            if (listaRemota.isNotEmpty()) {
                productoDao.deleteAll()
                productoDao.insertAll(listaRemota)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}