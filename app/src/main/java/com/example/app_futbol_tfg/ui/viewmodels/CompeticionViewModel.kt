package com.example.app_futbol_tfg.ui.viewmodels

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
    suspend fun getById(id: Int): CompeticionEntity? = repository.getById(id)
    fun insertCompeticion(nombre: String, idPais: Int?) {
        viewModelScope.launch {
            repository.insertCompeticion(
                CompeticionEntity(
                    nombre = nombre,
                    idPais = idPais
                )
            )
        }
    }
    fun updateCompeticion(competicion: CompeticionEntity) {
        viewModelScope.launch {
            repository.updateCompeticion(competicion)
        }
    }
    fun deleteCompeticion(competicion: CompeticionEntity) {
        viewModelScope.launch {
            repository.deleteCompeticion(competicion)
        }
    }
}