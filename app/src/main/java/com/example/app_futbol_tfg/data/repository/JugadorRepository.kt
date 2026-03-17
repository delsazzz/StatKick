package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.JugadorDao
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

class JugadorRepository(
    private val jugadorDao: JugadorDao
) {

    fun getAll(): Flow<List<JugadorEntity>> =
        jugadorDao.getAll()
    
    fun getAllActivos(): Flow<List<JugadorEntity>> =
        jugadorDao.getAllActivos()

    fun getAllRetirados(): Flow<List<JugadorEntity>> =
        jugadorDao.getAllRetirados()

    fun getByEquipo(idEquipo: Int): Flow<List<JugadorEntity>> =
        jugadorDao.getByEquipo(idEquipo)

    fun getSinEquipo(): Flow<List<JugadorEntity>> =
        jugadorDao.getSinEquipo()
    
    suspend fun getById(id: Int): JugadorEntity? =
        jugadorDao.getById(id)

    suspend fun insertJugador(jugador: JugadorEntity): Long =
        jugadorDao.insert(jugador)

    suspend fun updateJugador(jugador: JugadorEntity) =
        jugadorDao.update(jugador)

    suspend fun deleteJugador(jugador: JugadorEntity) =
        jugadorDao.delete(jugador)
}