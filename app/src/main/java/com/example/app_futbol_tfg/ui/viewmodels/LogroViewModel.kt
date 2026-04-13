package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import com.example.app_futbol_tfg.data.repository.LogroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LogroViewModel(private val repository: LogroRepository) : ViewModel() {

    val logros: Flow<List<LogroEntity>> = repository.getAll()

    fun getLogrosByUsuario(idUsuario: Int): Flow<List<LogroEntity>> = repository.getLogrosByUsuario(idUsuario)

    suspend fun getById(id: Int): LogroEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertLogro(nombre: String, descripcion: String?) {
        viewModelScope.launch {
            try {
            repository.insertLogro(
                LogroEntity(
                    nombre = nombre,
                    descripcion = descripcion
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun asignarLogro(idUsuario: Int, idLogro: Int, fechaObtenido: String) {
        viewModelScope.launch {
            try {
            repository.asignarLogro(
                UsuarioLogroEntity(
                    idUsuario = idUsuario,
                    idLogro = idLogro,
                    fechaObtenido = fechaObtenido
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}