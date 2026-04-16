package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.LogroDao
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class LogroRepository(
    private val logroDao: LogroDao
) {
    companion object {
        private const val TAG = "LogroRepository"
    }
    fun getAll(): Flow<List<LogroEntity>> = logroDao.getAll()
    suspend fun getById(id: Int): LogroEntity? =
        try {
            logroDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el logro con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el logro con id $id", e)
            null
        }
    fun getLogrosByUsuario(idUsuario: Int): Flow<List<LogroEntity>> = logroDao.getLogrosByUsuario(idUsuario)
    suspend fun insertLogro(logro: LogroEntity): Long =
        try {
            logroDao.insertLogro(logro)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el logro ${logro.nombre}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el logro ${logro.nombre}", e)
            -1L
        }
    suspend fun asignarLogro(relacion: UsuarioLogroEntity): Long =
        try {
            logroDao.asignarLogro(relacion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al asignar el logro ${relacion.idLogro} al usuario ${relacion.idUsuario}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al asignar el logro ${relacion.idLogro} al usuario ${relacion.idUsuario}", e)
            -1L
        }
}