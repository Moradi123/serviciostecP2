package com.example.serviciostec.model.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.serviciostec.model.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE usuario = :usuario AND contrasena = :contrasena LIMIT 1")
    suspend fun login(usuario: String, contrasena: String): UserEntity?

    @Query("SELECT * FROM users WHERE usuario = :usuario LIMIT 1")
    suspend fun obtenerPorUsuario(usuario: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id")
    fun getUserById(id: Int): Flow<UserEntity?>

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun obtenerUsuarioPorId(id: Int): UserEntity?

    @Query("SELECT COUNT(*) FROM users")
    suspend fun getUserCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)
}