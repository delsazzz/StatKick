package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.LocalidadDao
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class LocalidadRepository(
    private val localidadDao: LocalidadDao
) {
    companion object {
        private const val TAG = "LocalidadRepository"
    }
    fun getLocalidadesByPais(idPais: Int): Flow<List<LocalidadEntity>> = localidadDao.getByPais(idPais)
    suspend fun getLocalidadById(id: Int): LocalidadEntity? =
        try {
            localidadDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener la localidad con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener la localidad con id $id", e)
            null
        }
    fun getAll(): Flow<List<LocalidadEntity>> = localidadDao.getAll()
    suspend fun insertLocalidad(localidad: LocalidadEntity): Long =
        try {
            localidadDao.insert(localidad)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar la localidad ${localidad.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar la localidad ${localidad.nombre}", e)
            -1L
        }
    suspend fun updateLocalidad(localidad: LocalidadEntity) {
        try {
            localidadDao.update(localidad)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar la localidad con id ${localidad.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar la localidad con id ${localidad.id}", e)
        }
    }
    suspend fun deleteLocalidad(localidad: LocalidadEntity) {
        try {
            localidadDao.delete(localidad)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar la localidad con id ${localidad.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar la localidad con id ${localidad.id}", e)
        }
    }
}