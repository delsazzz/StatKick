package com.example.app_futbol_tfg.ui.viewmodels

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
    suspend fun getById(id: Int): LogroEntity? = repository.getById(id)
    fun insertLogro(nombre: String, descripcion: String?) {
        viewModelScope.launch {
            repository.insertLogro(
                LogroEntity(
                    nombre = nombre,
                    descripcion = descripcion
                )
            )
        }
    }
    fun asignarLogro(idUsuario: Int, idLogro: Int, fechaObtenido: String) {
        viewModelScope.launch {
            repository.asignarLogro(
                UsuarioLogroEntity(
                    idUsuario = idUsuario,
                    idLogro = idLogro,
                    fechaObtenido = fechaObtenido
                )
            )
        }
    }
}