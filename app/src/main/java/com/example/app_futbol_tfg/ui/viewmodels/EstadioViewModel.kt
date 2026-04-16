package com.example.app_futbol_tfg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import com.example.app_futbol_tfg.data.repository.EstadioRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EstadioViewModel(
    private val repository: EstadioRepository) : ViewModel() {
    val estadios: Flow<List<EstadioEntity>> = repository.getAll()
    fun getByPais(idPais: Int): Flow<List<EstadioEntity>> = repository.getByPais(idPais)
    fun getByLocalidad(idLocalidad: Int): Flow<List<EstadioEntity>> = repository.getByLocalidad(idLocalidad)
    suspend fun getById(id: Int): EstadioEntity? = repository.getById(id)
    fun insertEstadio(
        nombre: String,
        idLocalidad: Int?,
        idPais: Int?,
        capacidad: Int?
    ) {
        viewModelScope.launch {
            repository.insertEstadio(
                EstadioEntity(
                    nombre = nombre,
                    idLocalidad = idLocalidad,
                    idPais = idPais,
                    capacidad = capacidad
                )
            )
        }
    }
    fun updateEstadio(estadio: EstadioEntity) {
        viewModelScope.launch {
            repository.updateEstadio(estadio)
        }
    }
    fun deleteEstadio(estadio: EstadioEntity) {
        viewModelScope.launch {
            repository.deleteEstadio(estadio)
        }
    }
}