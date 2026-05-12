package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.EquipoDao
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class EquipoRepository(
    private val equipoDao: EquipoDao
) {
    companion object {
        private const val TAG = "EquipoRepository"
    }

    fun getAll(): Flow<List<EquipoEntity>> = equipoDao.getAll()
    suspend fun getById(id: Int): EquipoEntity? =
        try {
            equipoDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el equipo con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el equipo con id $id", e)
            null
        }

    fun getByPais(idPais: Int): Flow<List<EquipoEntity>> = equipoDao.getByPais(idPais)
    fun getSinPais(): Flow<List<EquipoEntity>> = equipoDao.getSinPais()
    suspend fun insertEquipo(equipo: EquipoEntity): Long =
        try {
            equipoDao.insert(equipo)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el equipo ${equipo.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el equipo ${equipo.nombre}", e)
            -1L
        }
    suspend fun updateEquipo(equipo: EquipoEntity) {
        try {
            equipoDao.update(equipo)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el equipo con id ${equipo.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el equipo con id ${equipo.id}", e)
        }
    }
    suspend fun deleteEquipo(equipo: EquipoEntity) {
        try {
            equipoDao.delete(equipo)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el equipo con id ${equipo.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el equipo con id ${equipo.id}", e)
        }
    }
}