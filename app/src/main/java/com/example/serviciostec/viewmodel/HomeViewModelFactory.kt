package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.serviciostec.model.data.repository.FormularioServicioRepository
import com.example.serviciostec.model.data.repository.UserRepository

class HomeViewModelFactory(
    private val formularioRepository: FormularioServicioRepository,
    private val userRepository: UserRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FormularioServicioViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FormularioServicioViewModel(formularioRepository) as T
        }
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}