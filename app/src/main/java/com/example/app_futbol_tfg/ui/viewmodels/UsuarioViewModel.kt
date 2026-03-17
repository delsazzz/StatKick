package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import com.example.app_futbol_tfg.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(
    private val repository: UsuarioRepository
) : ViewModel() {

    suspend fun getUsuarioById(id: Int): UsuarioEntity? =
        repository.getUsuarioById(id)

    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity? =
        repository.getByUsername(nombreUsuario)

    suspend fun getByEmail(email: String): UsuarioEntity? =
        repository.getByEmail(email)

    fun insertUsuario(
        nombreUsuario: String,
        email: String,
        passwordHash: String,
        fechaRegistro: String
    ) {
        viewModelScope.launch {
            repository.insertUsuario(
                UsuarioEntity(
                    nombreUsuario = nombreUsuario,
                    email = email,
                    passwordHash = passwordHash,
                    fechaRegistro = fechaRegistro
                )
            )
        }
    }

    fun updateUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.updateUsuario(usuario)
        }
    }

    fun deleteUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            repository.deleteUsuario(usuario)
        }
    }
}