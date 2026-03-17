package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.LogroDao
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import kotlinx.coroutines.flow.Flow

class LogroRepository(
    private val logroDao: LogroDao
) {

    fun getAll(): Flow<List<LogroEntity>> = logroDao.getAll()

    suspend fun getById(id: Int): LogroEntity? = logroDao.getById(id)

    fun getLogrosByUsuario(idUsuario: Int): Flow<List<LogroEntity>> =
        logroDao.getLogrosByUsuario(idUsuario)

    suspend fun insertLogro(logro: LogroEntity): Long =
        logroDao.insertLogro(logro)

    suspend fun asignarLogro(relacion: UsuarioLogroEntity): Long =
        logroDao.asignarLogro(relacion)
}