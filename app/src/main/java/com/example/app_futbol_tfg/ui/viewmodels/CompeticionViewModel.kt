package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import com.example.app_futbol_tfg.data.repository.CompeticionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CompeticionViewModel(private val repository: CompeticionRepository) : ViewModel() {

    val competiciones: Flow<List<CompeticionEntity>> = repository.getAll()

    fun getByPais(idPais: Int): Flow<List<CompeticionEntity>> = repository.getByPais(idPais)

    fun getSinPais(): Flow<List<CompeticionEntity>> = repository.getSinPais()

    suspend fun getById(id: Int): CompeticionEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun insertCompeticion(nombre: String, idPais: Int?) {
        viewModelScope.launch {
            try {
            repository.insertCompeticion(
                CompeticionEntity(
                    nombre = nombre,
                    idPais = idPais
                )
            )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateCompeticion(competicion: CompeticionEntity) {
        viewModelScope.launch {
            try {
            repository.updateCompeticion(competicion)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteCompeticion(competicion: CompeticionEntity) {
        viewModelScope.launch {
            try {
            repository.deleteCompeticion(competicion)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}