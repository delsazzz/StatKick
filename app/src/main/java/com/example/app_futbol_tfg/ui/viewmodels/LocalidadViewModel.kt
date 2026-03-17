package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import com.example.app_futbol_tfg.data.repository.LocalidadRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class LocalidadViewModel(
    private val repository: LocalidadRepository
) : ViewModel() {

    val localidades: Flow<List<LocalidadEntity>> = repository.getAll()

    fun getLocalidadesByPais(idPais: Int): Flow<List<LocalidadEntity>> =
        repository.getLocalidadesByPais(idPais)

    suspend fun getLocalidadById(id: Int): LocalidadEntity? =
        repository.getLocalidadById(id)

    fun insertLocalidad(nombre: String, idPais: Int) {
        viewModelScope.launch {
            repository.insertLocalidad(
                LocalidadEntity(
                    nombre = nombre,
                    idPais = idPais
                )
            )
        }
    }

    fun updateLocalidad(localidad: LocalidadEntity) {
        viewModelScope.launch {
            repository.updateLocalidad(localidad)
        }
    }

    fun deleteLocalidad(localidad: LocalidadEntity) {
        viewModelScope.launch {
            repository.deleteLocalidad(localidad)
        }
    }
}