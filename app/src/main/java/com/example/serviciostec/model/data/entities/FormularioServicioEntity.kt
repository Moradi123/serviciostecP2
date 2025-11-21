package com.example.serviciostec.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "servicio_formulario")
data class FormularioServicioEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombreCliente: String,
    val patente: String,
    val tipoServicio: String,
    val fecha: String,
    val fotoUri: String? = null
)
