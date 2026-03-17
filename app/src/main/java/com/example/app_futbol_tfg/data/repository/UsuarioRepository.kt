package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.UsuarioDao
import com.example.app_futbol_tfg.data.entity.UsuarioEntity

class UsuarioRepository(
    private val usuarioDao: UsuarioDao
) {

    suspend fun getById(id: Int): UsuarioEntity? =
        usuarioDao.getById(id)

    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity? =
        usuarioDao.getByUsername(nombreUsuario)

    suspend fun getByEmail(email: String): UsuarioEntity? =
        usuarioDao.getByEmail(email)

    suspend fun insertUsuario(usuario: UsuarioEntity): Long =
        usuarioDao.insert(usuario)

    suspend fun updateUsuario(usuario: UsuarioEntity) =
        usuarioDao.update(usuario)

    suspend fun deleteUsuario(usuario: UsuarioEntity) =
        usuarioDao.delete(usuario)
}