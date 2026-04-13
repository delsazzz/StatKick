package com.example.app_futbol_tfg.ui.viewmodel

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

    suspend fun getById(id: Int): PartidoEntity? {
        return try {
            repository.getById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

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
            try {
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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updatePartido(partido: PartidoEntity) {
        viewModelScope.launch {
            try {
                repository.updatePartido(partido)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deletePartido(partido: PartidoEntity) {
        viewModelScope.launch {
            try {
            repository.deletePartido(partido)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}