package com.example.serviciostec.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.serviciostec.model.data.entities.FormularioServicioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FormularioServicioDao {
    @Query("SELECT * FROM servicio_formulario")
    fun obtenerTodos(): Flow<List<FormularioServicioEntity>>

    @Insert
    suspend fun insertarServicio(servicio: FormularioServicioEntity)
}