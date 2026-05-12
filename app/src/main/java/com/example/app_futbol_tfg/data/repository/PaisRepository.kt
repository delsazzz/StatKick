package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PaisDao
import com.example.app_futbol_tfg.data.entity.PaisEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class PaisRepository(
    private val paisDao: PaisDao
) {
    companion object {
        private const val TAG = "PaisRepository"
    }
    fun getAll(): Flow<List<PaisEntity>> = paisDao.getAll()
    suspend fun getPaisById(id: Int): PaisEntity? =
        try {
            paisDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el país con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el país con id $id", e)
            null
        }
    suspend fun getByNombre(nombre: String): PaisEntity? =
        try {
            paisDao.getByNombre(nombre)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el país con nombre $nombre", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el país con nombre $nombre", e)
            null
        }
    suspend fun insertPais(pais: PaisEntity): Long =
        try {
            paisDao.insert(pais)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el país ${pais.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el país ${pais.nombre}", e)
            -1L
        }
    suspend fun updatePais(pais: PaisEntity) {
        try {
            paisDao.update(pais)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el país con id ${pais.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el país con id ${pais.id}", e)
        }
    }
    suspend fun deletePais(pais: PaisEntity) {
        try {
            paisDao.delete(pais)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el país con id ${pais.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el país con id ${pais.id}", e)
        }
    }

}