package com.example.serviciostec.model.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.serviciostec.model.data.entities.VehiculoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VehiculoDao {
    @Query("SELECT * FROM vehiculos WHERE usuarioId = :usuarioId")
    fun obtenerVehiculosPorUsuario(usuarioId: String): Flow<List<VehiculoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarVehiculo(vehiculo: VehiculoEntity)

    @Delete
    suspend fun eliminarVehiculo(vehiculo: VehiculoEntity)
}