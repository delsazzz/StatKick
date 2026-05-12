package com.example.app_futbol_tfg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.repository.PartidoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PartidoViewModel(private val repository: PartidoRepository) : ViewModel() {
    val partidos: Flow<List<PartidoEntity>> = repository.getAll()
    fun getByEquipo(idEquipo: Int): Flow<List<PartidoEntity>> = repository.getByEquipo(idEquipo)
    fun getByCompeticionYTemporada(idCompeticion: Int, idTemporada: Int): Flow<List<PartidoEntity>> =
        repository.getByCompeticionYTemporada(idCompeticion, idTemporada)
    fun getByEstadio(idEstadio: Int): Flow<List<PartidoEntity>> = repository.getByEstadio(idEstadio)
    suspend fun getById(id: Int): PartidoEntity? = repository.getById(id)
    fun insertPartido(
        idEquipoLocal: Int,
        idEquipoVisitante: Int,
        golesLocal: Int,
        golesVisitante: Int,
        fecha: String,
        idTemporada: Int?,
        idCompeticion: Int?,
        idEstadio: Int?,
        jornada: String?
    ) {
        viewModelScope.launch {
                repository.insertPartido(
                    PartidoEntity(
                        idEquipoLocal = idEquipoLocal,
                        idEquipoVisitante = idEquipoVisitante,
                        golesLocal = golesLocal,
                        golesVisitante = golesVisitante,
                        fecha = fecha,
                        idTemporada = idTemporada,
                        idCompeticion = idCompeticion,
                        idEstadio = idEstadio,
                        jornada = jornada
                    )
                )
        }
    }
    fun updatePartido(partido: PartidoEntity) {
        viewModelScope.launch {
                repository.updatePartido(partido)
        }
    }
    fun deletePartido(partido: PartidoEntity) {
        viewModelScope.launch {
            repository.deletePartido(partido)
        }
    }
}