package com.example.serviciostec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviciostec.model.data.config.AppDatabase
import com.example.serviciostec.model.data.repository.FormularioServicioRepository
import com.example.serviciostec.model.data.repository.ProductoRepository
import com.example.serviciostec.model.data.repository.UserRepository
import com.example.serviciostec.navigation.AppNavigation
import com.example.serviciostec.ui.theme.ServiciosTecTheme
import com.example.serviciostec.viewmodel.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val vehiculoDao = db.vehiculoDao()
        val productoDao = db.productoDao()

        val formRepository = FormularioServicioRepository(db.formularioServicioDao())
        val userRepository = UserRepository(db.userDao())
        val productoRepository = ProductoRepository(productoDao)

        val factory = HomeViewModelFactory(formRepository, userRepository)
        val formViewModel = ViewModelProvider(this, factory)[FormularioServicioViewModel::class.java]
        val userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        val vehiculoViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VehiculoViewModel(vehiculoDao) as T
            }
        })[VehiculoViewModel::class.java]

        val productoViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductoViewModel(productoRepository) as T
            }
        })[ProductoViewModel::class.java]

        setContent {
            ServiciosTecTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(
                        formViewModel = formViewModel,
                        userViewModel = userViewModel,
                        vehiculoViewModel = vehiculoViewModel,
                        productoViewModel = productoViewModel
                    )
                }
            }
        }
    }
}