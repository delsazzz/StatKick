package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PartidoJugadorDao
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class PartidoJugadorRepository(
    private val partidoJugadorDao: PartidoJugadorDao
) {
    companion object {
        private const val TAG = "PartidoJugadorRepository"
    }
    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>> = partidoJugadorDao.getByPartido(idPartido)
    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>> = partidoJugadorDao.getByJugador(idJugador)
    suspend fun getTotalGoles(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalGoles(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener los goles totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener los goles totales del jugador $idJugador", e)
            0
        }
    suspend fun getTotalAsistencias(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalAsistencias(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener las asistencias totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener las asistencias totales del jugador $idJugador", e)
            0
        }
    suspend fun getTotalAmarillas(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalAmarillas(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener las amarillas totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener las amarillas totales del jugador $idJugador", e)
            0
        }
    suspend fun getTotalRojas(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalRojas(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener las rojas totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener las rojas totales del jugador $idJugador", e)
            0
        }
    suspend fun getTotalMinutos(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalMinutos(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener los minutos totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener los minutos totales del jugador $idJugador", e)
            0
        }
    suspend fun getTotalPartidos(idJugador: Int): Int =
        try {
            partidoJugadorDao.getTotalPartidos(idJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener los partidos totales del jugador $idJugador", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener los partidos totales del jugador $idJugador", e)
            0
        }
    suspend fun insertParticipacion(partidoJugador: PartidoJugadorEntity): Long =
        try {
            partidoJugadorDao.insert(partidoJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
            -1L
        }
    suspend fun updateParticipacion(partidoJugador: PartidoJugadorEntity) {
        try {
            partidoJugadorDao.update(partidoJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
        }
    }
    suspend fun deleteParticipacion(partidoJugador: PartidoJugadorEntity) {
        try {
            partidoJugadorDao.delete(partidoJugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar la participación del jugador ${partidoJugador.idJugador} en el partido ${partidoJugador.idPartido}", e)
        }
    }
}