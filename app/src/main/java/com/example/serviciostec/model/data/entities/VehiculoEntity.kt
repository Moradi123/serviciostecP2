package com.example.serviciostec.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vehiculos")
data class VehiculoEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val marca: String,
    val modelo: String,
    val anio: String,
    val patente: String,
    val usuarioId: String
)