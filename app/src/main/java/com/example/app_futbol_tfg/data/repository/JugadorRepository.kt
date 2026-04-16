package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.JugadorDao
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class JugadorRepository(
    private val jugadorDao: JugadorDao
) {
    companion object {
        private const val TAG = "JugadorRepository"
    }
    fun getAll(): Flow<List<JugadorEntity>> = jugadorDao.getAll()
    fun getAllActivos(): Flow<List<JugadorEntity>> = jugadorDao.getAllActivos()
    fun getAllRetirados(): Flow<List<JugadorEntity>> = jugadorDao.getAllRetirados()
    fun getByEquipo(idEquipo: Int): Flow<List<JugadorEntity>> = jugadorDao.getByEquipo(idEquipo)
    fun getSinEquipo(): Flow<List<JugadorEntity>> = jugadorDao.getSinEquipo()
    suspend fun getById(id: Int): JugadorEntity? =
        try {
            jugadorDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el jugador con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el jugador con id $id", e)
            null
        }
    suspend fun insertJugador(jugador: JugadorEntity): Long =
        try {
            jugadorDao.insert(jugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el jugador ${jugador.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el jugador ${jugador.nombre}", e)
            -1L
        }
    suspend fun updateJugador(jugador: JugadorEntity) {
        try {
            jugadorDao.update(jugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el jugador con id ${jugador.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el jugador con id ${jugador.id}", e)
        }
    }
    suspend fun deleteJugador(jugador: JugadorEntity) {
        try {
            jugadorDao.delete(jugador)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el jugador con id ${jugador.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el jugador con id ${jugador.id}", e)
        }
    }
}