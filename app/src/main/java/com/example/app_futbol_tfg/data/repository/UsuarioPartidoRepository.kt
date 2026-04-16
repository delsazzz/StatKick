package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.UsuarioPartidoDao
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity
import kotlinx.coroutines.flow.Flow
import android.database.sqlite.SQLiteException
import android.util.Log

class UsuarioPartidoRepository(
    private val usuarioPartidoDao: UsuarioPartidoDao
) {
    companion object {
        private const val TAG = "UsuarioPartidoRepository"
    }
    fun getPartidosByUsuario(idUsuario: Int): Flow<List<PartidoEntity>> = usuarioPartidoDao.getPartidosByUsuario(idUsuario)
    suspend fun countByUsuario(idUsuario: Int): Int =
        try {
            usuarioPartidoDao.countByUsuario(idUsuario)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al contar los partidos del usuario $idUsuario", e)
            0
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al contar los partidos del usuario $idUsuario", e)
            0
        }
    suspend fun getRelacion(idUsuario: Int, idPartido: Int): UsuarioPartidoEntity? =
        try {
            usuarioPartidoDao.getRelacion(idUsuario, idPartido)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener la relación del usuario $idUsuario con el partido $idPartido", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener la relación del usuario $idUsuario con el partido $idPartido", e)
            null
        }
    suspend fun insertRelacion(relacion: UsuarioPartidoEntity): Long =
        try {
            usuarioPartidoDao.insert(relacion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar la relación usuario-partido (${relacion.idUsuario}, ${relacion.idPartido})", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar la relación usuario-partido (${relacion.idUsuario}, ${relacion.idPartido})", e)
            -1L
        }
    suspend fun deleteRelacion(relacion: UsuarioPartidoEntity) {
        try {
            usuarioPartidoDao.delete(relacion)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar la relación usuario-partido (${relacion.idUsuario}, ${relacion.idPartido})", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar la relación usuario-partido (${relacion.idUsuario}, ${relacion.idPartido})", e)
        }
    }
}