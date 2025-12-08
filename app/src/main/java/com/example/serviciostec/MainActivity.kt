package com.example.serviciostec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviciostec.model.data.config.AppDatabase
import com.example.serviciostec.model.data.repository.FormularioServicioRepository
import com.example.serviciostec.model.data.repository.UserRepository
import com.example.serviciostec.navigation.AppNavigation
import com.example.serviciostec.viewmodel.FormularioServicioViewModel
import com.example.serviciostec.viewmodel.HomeViewModelFactory
import com.example.serviciostec.viewmodel.UserViewModel
import com.example.serviciostec.viewmodel.VehiculoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)

        val formRepository = FormularioServicioRepository(db.formularioServicioDao())

        val userRepository = UserRepository(db.userDao())

        val factory = HomeViewModelFactory(formRepository, userRepository)

        val formViewModel = ViewModelProvider(this, factory)[FormularioServicioViewModel::class.java]

        val userViewModel = ViewModelProvider(this, factory)[UserViewModel::class.java]

        val vehiculoDao = db.vehiculoDao()

        val vehiculoViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return VehiculoViewModel(vehiculoDao) as T
            }
        })[VehiculoViewModel::class.java]

        setContent {
            AppNavigation(
                formViewModel = formViewModel,
                userViewModel = userViewModel,
                vehiculoViewModel = vehiculoViewModel
            )
        }
    }
}