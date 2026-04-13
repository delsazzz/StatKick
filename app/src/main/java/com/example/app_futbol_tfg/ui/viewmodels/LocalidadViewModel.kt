package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import com.example.app_futbol_tfg.data.repository.LocalidadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocalidadViewModel(private val repository: LocalidadRepository) : ViewModel() {

    val localidades: Flow<List<LocalidadEntity>> = repository.getAll()

    fun getLocalidadesByPais(idPais: Int): Flow<List<LocalidadEntity>> = repository.getLocalidadesByPais(idPais)

    suspend fun getLocalidadById(id: Int): LocalidadEntity? {
        return try {
            repository.getLocalidadById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertLocalidad(nombre: String, idPais: Int) {
        viewModelScope.launch {
            try {
            repository.insertLocalidad(
                LocalidadEntity(
                    nombre = nombre,
                    idPais = idPais
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateLocalidad(localidad: LocalidadEntity) {
        viewModelScope.launch {
            try {
            repository.updateLocalidad(localidad)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteLocalidad(localidad: LocalidadEntity) {
        viewModelScope.launch {
            try {
            repository.deleteLocalidad(localidad)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}