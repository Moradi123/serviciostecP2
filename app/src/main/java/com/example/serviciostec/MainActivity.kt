package com.example.serviciostec

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProvider
import com.example.serviciostec.model.data.config.AppDatabase
import com.example.serviciostec.model.data.repository.FormularioServicioRepository
import com.example.serviciostec.model.data.repository.UserRepository
import com.example.serviciostec.navigation.AppNavigation
import com.example.serviciostec.viewmodel.FormularioServicioViewModel
import com.example.serviciostec.viewmodel.HomeViewModelFactory
import com.example.serviciostec.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(applicationContext)
        val formRepository = FormularioServicioRepository(db.formularioServicioDao())
        val userRepository = UserRepository(db.userDao())
        val factory = HomeViewModelFactory(formRepository, userRepository)
        val formViewModel = ViewModelProvider(this, factory).get(FormularioServicioViewModel::class.java)
        val userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)

        setContent {
            AppNavigation(
                formViewModel = formViewModel,
                userViewModel = userViewModel
            )
        }
    }
}