package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.EstadioDao
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class EstadioRepository(
    private val estadioDao: EstadioDao
) {
    companion object {
        private const val TAG = "EstadioRepository"
    }
    fun getAll(): Flow<List<EstadioEntity>> = estadioDao.getAll()
    suspend fun getById(id: Int): EstadioEntity? =
        try {
            estadioDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el estadio con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el estadio con id $id", e)
            null
        }
    fun getByPais(idPais: Int): Flow<List<EstadioEntity>> = estadioDao.getByPais(idPais)
    fun getByLocalidad(idLocalidad: Int): Flow<List<EstadioEntity>> = estadioDao.getByLocalidad(idLocalidad)
    suspend fun insertEstadio(estadio: EstadioEntity): Long =
        try {
            estadioDao.insert(estadio)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el estadio ${estadio.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el estadio ${estadio.nombre}", e)
            -1L
        }
    suspend fun updateEstadio(estadio: EstadioEntity) {
        try {
            estadioDao.update(estadio)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el estadio con id ${estadio.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el estadio con id ${estadio.id}", e)
        }
    }
    suspend fun deleteEstadio(estadio: EstadioEntity) {
        try {
            estadioDao.delete(estadio)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el estadio con id ${estadio.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el estadio con id ${estadio.id}", e)
        }
    }
}