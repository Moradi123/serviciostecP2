package com.example.serviciostec.model.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.serviciostec.model.data.dao.FormularioServicioDao
import com.example.serviciostec.model.data.dao.ProductoDao
import com.example.serviciostec.model.data.dao.UserDao
import com.example.serviciostec.model.data.dao.VehiculoDao
import com.example.serviciostec.model.data.entities.FormularioServicioEntity
import com.example.serviciostec.model.data.entities.ProductoEntity
import com.example.serviciostec.model.data.entities.UserEntity
import com.example.serviciostec.model.data.entities.VehiculoEntity


@Database(entities = [
    UserEntity::class,
    ProductoEntity::class,
    VehiculoEntity::class,
    FormularioServicioEntity::class
], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun productoDao(): ProductoDao
    abstract fun vehiculoDao(): VehiculoDao
    abstract fun formularioServicioDao(): FormularioServicioDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}