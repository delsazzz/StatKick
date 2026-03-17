package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.UsuarioPartidoDao
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity
import kotlinx.coroutines.flow.Flow

class UsuarioPartidoRepository(
    private val usuarioPartidoDao: UsuarioPartidoDao
) {

    fun getPartidosByUsuario(idUsuario: Int): Flow<List<PartidoEntity>> =
        usuarioPartidoDao.getPartidosByUsuario(idUsuario)

    suspend fun countByUsuario(idUsuario: Int): Int =
        usuarioPartidoDao.countByUsuario(idUsuario)

    suspend fun getRelacion(idUsuario: Int, idPartido: Int): UsuarioPartidoEntity? =
        usuarioPartidoDao.getRelacion(idUsuario, idPartido)

    suspend fun insertRelacion(relacion: UsuarioPartidoEntity): Long =
        usuarioPartidoDao.insert(relacion)

    suspend fun deleteRelacion(relacion: UsuarioPartidoEntity) =
        usuarioPartidoDao.delete(relacion)
}