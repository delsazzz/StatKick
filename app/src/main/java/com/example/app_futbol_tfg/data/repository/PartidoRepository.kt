package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PartidoDao
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class PartidoRepository(
    private val partidoDao: PartidoDao
) {
    companion object {
        private const val TAG = "PartidoRepository"
    }
    fun getAll(): Flow<List<PartidoEntity>> = partidoDao.getAll()
    suspend fun getById(id: Int): PartidoEntity? =
        try {
            partidoDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el partido con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el partido con id $id", e)
            null
        }
    fun getByEquipo(idEquipo: Int): Flow<List<PartidoEntity>> = partidoDao.getByEquipo(idEquipo)
    fun getByCompeticionYTemporada(idCompeticion: Int, idTemporada: Int): Flow<List<PartidoEntity>> =
        partidoDao.getByCompeticionYTemporada(idCompeticion, idTemporada)
    fun getByEstadio(idEstadio: Int): Flow<List<PartidoEntity>> = partidoDao.getByEstadio(idEstadio)
    suspend fun insertPartido(partido: PartidoEntity): Long =
        try {
            partidoDao.insert(partido)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el partido con fecha ${partido.fecha}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el partido con fecha ${partido.fecha}", e)
            -1L
        }
    suspend fun updatePartido(partido: PartidoEntity) {
        try {
            partidoDao.update(partido)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el partido con id ${partido.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el partido con id ${partido.id}", e)
        }
    }
    suspend fun deletePartido(partido: PartidoEntity) {
        try {
            partidoDao.delete(partido)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el partido con id ${partido.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el partido con id ${partido.id}", e)
        }
    }
}