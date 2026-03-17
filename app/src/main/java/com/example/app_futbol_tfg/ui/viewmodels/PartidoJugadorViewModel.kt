package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import com.example.app_futbol_tfg.data.repository.PartidoJugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class PartidoJugadorViewModel(
    private val repository: PartidoJugadorRepository
) : ViewModel() {

    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>> =
        repository.getByPartido(idPartido)

    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>> =
        repository.getByJugador(idJugador)

    suspend fun getTotalGoles(idJugador: Int): Int =
        repository.getTotalGoles(idJugador)

    suspend fun getTotalAsistencias(idJugador: Int): Int =
        repository.getTotalAsistencias(idJugador)

    suspend fun getTotalAmarillas(idJugador: Int): Int =
        repository.getTotalAmarillas(idJugador)

    suspend fun getTotalRojas(idJugador: Int): Int =
        repository.getTotalRojas(idJugador)

    suspend fun getTotalMinutos(idJugador: Int): Int =
        repository.getTotalMinutos(idJugador)

    suspend fun getTotalPartidos(idJugador: Int): Int =
        repository.getTotalPartidos(idJugador)

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
        }
    }

    fun updateParticipacion(partidoJugador: PartidoJugadorEntity) {
        viewModelScope.launch {
            repository.updateParticipacion(partidoJugador)
        }
    }

    fun deleteParticipacion(partidoJugador: PartidoJugadorEntity) {
        viewModelScope.launch {
            repository.deleteParticipacion(partidoJugador)
        }
    }
}