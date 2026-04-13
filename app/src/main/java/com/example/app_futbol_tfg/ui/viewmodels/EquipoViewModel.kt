package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import com.example.app_futbol_tfg.data.repository.EquipoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class EquipoViewModel(private val repository: EquipoRepository) : ViewModel() {

    val equipos: Flow<List<EquipoEntity>> = repository.getAll()

    fun getByPais(idPais: Int): Flow<List<EquipoEntity>> = repository.getByPais(idPais)

    fun getSinPais(): Flow<List<EquipoEntity>> = repository.getSinPais()

    suspend fun getById(id: Int): EquipoEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertEquipo(
        nombre: String,
        anioFundacion: Int?,
        idEstadio: Int?,
        idLocalidad: Int?,
        idPais: Int?
    ) {
        viewModelScope.launch {
            try {
            repository.insertEquipo(
                EquipoEntity(
                    nombre = nombre,
                    anioFundacion = anioFundacion,
                    idEstadio = idEstadio,
                    idLocalidad = idLocalidad,
                    idPais = idPais
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateEquipo(equipo: EquipoEntity) {
        viewModelScope.launch {
            try {
            repository.updateEquipo(equipo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteEquipo(equipo: EquipoEntity) {
        viewModelScope.launch {
            try {
            repository.deleteEquipo(equipo)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}