package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import com.example.app_futbol_tfg.data.repository.PartidoJugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PartidoJugadorViewModel(private val repository: PartidoJugadorRepository) : ViewModel() {

    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>> = repository.getByPartido(idPartido)

    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>> = repository.getByJugador(idJugador)

    suspend fun getTotalGoles(idJugador: Int): Int {
        return try {
            repository.getTotalGoles(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalAsistencias(idJugador: Int): Int {
        return try {
            repository.getTotalAsistencias(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalAmarillas(idJugador: Int): Int {
        return try {
            repository.getTotalAmarillas(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalRojas(idJugador: Int): Int {
        return try {
            repository.getTotalRojas(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalMinutos(idJugador: Int): Int {
        return try {
            repository.getTotalMinutos(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    suspend fun getTotalPartidos(idJugador: Int): Int {
        return try {
            repository.getTotalPartidos(idJugador)
        } catch (e: Exception) {
            e.printStackTrace()
            0
        }
    }

    fun insertParticipacion(
        idPartido: Int,
        idJugador: Int,
        idEquipo: Int,
        titular: Boolean = false,
        minutosJugados: Int?,
        goles: Int = 0,
        asistencias: Int = 0,
        amarillas: Int = 0,
        rojas: Int = 0
    ) {
        viewModelScope.launch {
            try {
                repository.insertParticipacion(
                    PartidoJugadorEntity(
                        idPartido = idPartido,
                        idJugador = idJugador,
                        idEquipo = idEquipo,
                        titular = titular,
                        minutosJugados = minutosJugados,
                        goles = goles,
                        asistencias = asistencias,
                        amarillas = amarillas,
                        rojas = rojas
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateParticipacion(partidoJugador: PartidoJugadorEntity) {
        viewModelScope.launch {
            try {
            repository.updateParticipacion(partidoJugador)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteParticipacion(partidoJugador: PartidoJugadorEntity) {
        viewModelScope.launch {
            try {
            repository.deleteParticipacion(partidoJugador)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}