package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PartidoJugadorDao
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import kotlinx.coroutines.flow.Flow

class PartidoJugadorRepository(
    private val partidoJugadorDao: PartidoJugadorDao
) {

    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>> =
        partidoJugadorDao.getByPartido(idPartido)

    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>> =
        partidoJugadorDao.getByJugador(idJugador)

    suspend fun getTotalGoles(idJugador: Int): Int =
        partidoJugadorDao.getTotalGoles(idJugador)

    suspend fun getTotalAsistencias(idJugador: Int): Int =
        partidoJugadorDao.getTotalAsistencias(idJugador)

    suspend fun getTotalAmarillas(idJugador: Int): Int =
        partidoJugadorDao.getTotalAmarillas(idJugador)

    suspend fun getTotalRojas(idJugador: Int): Int =
        partidoJugadorDao.getTotalRojas(idJugador)

    suspend fun getTotalMinutos(idJugador: Int): Int =
        partidoJugadorDao.getTotalMinutos(idJugador)

    suspend fun getTotalPartidos(idJugador: Int): Int = 
        partidoJugadorDao.getTotalPartidos(idJugador)

    suspend fun insertParticipacion(partidoJugador: PartidoJugadorEntity): Long =
        partidoJugadorDao.insert(partidoJugador)

    suspend fun updateParticipacion(partidoJugador: PartidoJugadorEntity) =
        partidoJugadorDao.update(partidoJugador)

    suspend fun deleteParticipacion(partidoJugador: PartidoJugadorEntity) =
        partidoJugadorDao.delete(partidoJugador)
}