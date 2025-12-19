package com.example.serviciostec.model.data.repository

import com.example.serviciostec.model.data.dao.UserDao
import com.example.serviciostec.model.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    suspend fun login(usuario: String, contrasena: String): UserEntity? {
        return userDao.login(usuario, contrasena)
    }

    fun getUserById(id: Int): Flow<UserEntity?> {
        return userDao.getUserById(id)
    }

    suspend fun obtenerUsuarioPorId(id: Int): UserEntity? {
        return userDao.obtenerUsuarioPorId(id)
    }
    suspend fun updateUser(user: UserEntity) {
        userDao.updateUser(user)
    }

    suspend fun ensureAdminExists() {
        if (userDao.getUserCount() == 0) {
            userDao.insertUser(
                UserEntity(
                    nombre = "Administrador",
                    apellido = "Sistema",
                    telefono = "+56 9 0000 0000",
                    usuario = "admin",
                    contrasena = "1234"
                )
            )
        }
    }
}