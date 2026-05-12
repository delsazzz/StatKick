package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.CompeticionDao
import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class CompeticionRepository(
    private val competicionDao: CompeticionDao
) {
    companion object {
        private const val TAG = "CompeticionRepository"
    }
    fun getAll(): Flow<List<CompeticionEntity>> = competicionDao.getAll()
    suspend fun getById(id: Int): CompeticionEntity? =
        try {
            competicionDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener la competición con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener la competición con id $id", e)
            null
        }
    fun getByPais(idPais: Int): Flow<List<CompeticionEntity>> = competicionDao.getByPais(idPais)
    fun getSinPais(): Flow<List<CompeticionEntity>> = competicionDao.getSinPais()
    suspend fun insertCompeticion(competicion: CompeticionEntity): Long =
        try {
            competicionDao.insert(competicion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar la competición ${competicion.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar la competición ${competicion.nombre}", e)
            -1L
        }
    suspend fun updateCompeticion(competicion: CompeticionEntity) {
        try {
            competicionDao.update(competicion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar la competición con id ${competicion.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar la competición con id ${competicion.id}", e)
        }
    }
    suspend fun deleteCompeticion(competicion: CompeticionEntity) {
        try {
            competicionDao.delete(competicion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar la competición con id ${competicion.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar la competición con id ${competicion.id}", e)
        }
    }
}