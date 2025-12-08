package com.example.serviciostec.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serviciostec.model.data.entities.UserEntity
import com.example.serviciostec.model.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    private val _currentUser = MutableStateFlow<UserEntity?>(null)
    val currentUser: StateFlow<UserEntity?> = _currentUser.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null)
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    init {
        viewModelScope.launch {
            repository.ensureAdminExists()
        }
    }

    fun login(usuario: String, contrasena: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            val user = repository.login(usuario, contrasena)
            if (user != null) {
                _currentUser.value = user
                _loginError.value = null
                onSuccess()
            } else {
                _loginError.value = "Usuario o contraseña incorrectos"
            }
        }
    }

    fun updateUser(nombre: String, apellido: String, telefono: String, usuario: String, contrasena: String) {
        val current = _currentUser.value ?: return

        val updatedUser = current.copy(
            nombre = nombre,
            apellido = apellido,
            telefono = telefono,
            usuario = usuario,
            contrasena = contrasena
        )

        viewModelScope.launch {
            repository.updateUser(updatedUser)
            _currentUser.value = updatedUser
        }
    }

    fun logout() {
        _currentUser.value = null
    }

    fun loginWithGoogle(nombre: String, email: String, onSuccess: () -> Unit) {
        val googleUser = com.example.serviciostec.model.data.entities.UserEntity(
            nombre = nombre,
            apellido = "",
            telefono = "",
            usuario = email,
            contrasena = ""
        )

        _currentUser.value = googleUser
        _loginError.value = null
        onSuccess()
    }
}