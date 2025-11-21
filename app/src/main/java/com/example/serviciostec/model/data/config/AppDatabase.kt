package com.example.serviciostec.model.data.config

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.serviciostec.model.data.dao.FormularioServicioDao
import com.example.serviciostec.model.data.dao.UserDao
import com.example.serviciostec.model.data.entities.FormularioServicioEntity
import com.example.serviciostec.model.data.entities.UserEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [FormularioServicioEntity::class, UserEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun formularioServicioDao(): FormularioServicioDao
    abstract fun userDao(): UserDao // Agregamos el DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "servitecdb"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.userDao().insertUser(
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
                    })
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
