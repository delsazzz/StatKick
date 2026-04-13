package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity
import com.example.app_futbol_tfg.data.repository.UsuarioPartidoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UsuarioPartidoViewModel(private val repository: UsuarioPartidoRepository) : ViewModel() {

    fun getPartidosByUsuario(idUsuario: Int): Flow<List<PartidoEntity>> = repository.getPartidosByUsuario(idUsuario)

    suspend fun countByUsuario(idUsuario: Int): Int {
        return try {
            repository.countByUsuario(idUsuario)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getRelacion(idUsuario: Int, idPartido: Int): UsuarioPartidoEntity? {
        return try {
            repository.getRelacion(idUsuario, idPartido)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertRelacion(idUsuario: Int, idPartido: Int, fechaRegistro: String) {
        viewModelScope.launch {
            try {
                repository.insertRelacion(
                    UsuarioPartidoEntity(
                        idUsuario = idUsuario,
                        idPartido = idPartido,
                        fechaRegistro = fechaRegistro
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteRelacion(relacion: UsuarioPartidoEntity) {
        viewModelScope.launch {
            try {
            repository.deleteRelacion(relacion)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}