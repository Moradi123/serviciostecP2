package com.example.serviciostec.model.data.repository

import com.example.serviciostec.model.data.dao.FormularioServicioDao
import com.example.serviciostec.model.data.entities.FormularioServicioEntity
import kotlinx.coroutines.flow.Flow

class FormularioServicioRepository(private val dao: FormularioServicioDao) {
    val servicios: Flow<List<FormularioServicioEntity>> = dao.obtenerTodos()
    suspend fun guardarServicio(servicio: FormularioServicioEntity) {
        dao.insertarServicio(servicio)
    }
    suspend fun actualizarEstado(id: Int, nuevoEstado: String) {
        dao.actualizarEstado(id, nuevoEstado)
    }
}