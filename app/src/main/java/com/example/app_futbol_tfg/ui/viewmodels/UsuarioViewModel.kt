package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import com.example.app_futbol_tfg.data.repository.UsuarioRepository
import kotlinx.coroutines.launch

class UsuarioViewModel(private val repository: UsuarioRepository) : ViewModel() {

    suspend fun getUsuarioById(id: Int): UsuarioEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity? {
        return try {
            repository.getByUsername(nombreUsuario)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getByEmail(email: String): UsuarioEntity? {
        return try {
            repository.getByEmail(email)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertUsuario(
        nombreUsuario: String,
        email: String,
        passwordHash: String,
        fechaRegistro: String,
        rol: String = "usuario"
    ) {
        viewModelScope.launch {
            try {
                repository.insertUsuario(
                    UsuarioEntity(
                        nombreUsuario = nombreUsuario,
                        email = email,
                        passwordHash = passwordHash,
                        fechaRegistro = fechaRegistro,
                        rol = rol
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            try {
            repository.updateUsuario(usuario)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteUsuario(usuario: UsuarioEntity) {
        viewModelScope.launch {
            try {
                repository.deleteUsuario(usuario)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}