package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.TemporadaDao
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class TemporadaRepository(
    private val temporadaDao: TemporadaDao
) {
    companion object {
        private const val TAG = "TemporadaRepository"
    }
    fun getAll(): Flow<List<TemporadaEntity>> = temporadaDao.getAll()
    suspend fun getById(id: Int): TemporadaEntity? =
        try {
            temporadaDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener la temporada con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener la temporada con id $id", e)
            null
        }
    suspend fun getByTemporada(temporada: String): TemporadaEntity? =
        try {
            temporadaDao.getByTemporada(temporada)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener la temporada $temporada", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener la temporada $temporada", e)
            null
        }
    suspend fun insertTemporada(temporada: TemporadaEntity): Long =
        try {
            temporadaDao.insert(temporada)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar la temporada ${temporada.temporada}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar la temporada ${temporada.temporada}", e)
            -1L
        }
}