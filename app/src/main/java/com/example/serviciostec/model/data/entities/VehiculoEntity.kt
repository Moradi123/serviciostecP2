package com.example.serviciostec.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehiculos")
data class VehiculoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val marca: String,      // Ej: Toyota
    val modelo: String,     // Ej: Corolla
    val anio: String,       // Ej: 2018
    val patente: String,    // Ej: ABCD-12
    val usuarioId: String
)