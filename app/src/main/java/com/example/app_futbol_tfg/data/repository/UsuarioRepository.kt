package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.UsuarioDao
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import android.database.sqlite.SQLiteException
import android.util.Log

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {
    companion object {
        private const val TAG = "UsuarioRepository"
    }
    suspend fun getById(id: Int): UsuarioEntity? =
        try {
            usuarioDao.getById(id)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el usuario con id $id", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el usuario con id $id", e)
            null
        }
    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity? =
        try {
            usuarioDao.getByUsername(nombreUsuario)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el usuario con nombre $nombreUsuario", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el usuario con nombre $nombreUsuario", e)
            null
        }
    suspend fun getByEmail(email: String): UsuarioEntity? =
        try {
            usuarioDao.getByEmail(email)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al obtener el usuario con email $email", e)
            null
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al obtener el usuario con email $email", e)
            null
        }
    suspend fun insertUsuario(usuario: UsuarioEntity): Long =
        try {
            usuarioDao.insert(usuario)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al insertar el usuario ${usuario.nombreUsuario}", e)
            -1L
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al insertar el usuario ${usuario.nombreUsuario}", e)
            -1L
        }
    suspend fun updateUsuario(usuario: UsuarioEntity) {
        try {
            usuarioDao.update(usuario)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al actualizar el usuario con id ${usuario.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al actualizar el usuario con id ${usuario.id}", e)
        }
    }
    suspend fun deleteUsuario(usuario: UsuarioEntity) {
        try {
            usuarioDao.delete(usuario)
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al eliminar el usuario con id ${usuario.id}", e)
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Estado inválido al eliminar el usuario con id ${usuario.id}", e)
        }
    }
}