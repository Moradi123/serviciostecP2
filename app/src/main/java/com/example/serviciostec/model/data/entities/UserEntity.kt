package com.example.serviciostec.model.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val apellido: String,
    val telefono: String,
    val usuario: String,
    val contrasena: String,
    val photoUri: String? = null,
    val rol: String = "cliente")