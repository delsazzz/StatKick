package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PartidoDao
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import kotlinx.coroutines.flow.Flow

class PartidoRepository(
    private val partidoDao: PartidoDao
) {

    fun getAll(): Flow<List<PartidoEntity>> = partidoDao.getAll()

    suspend fun getById(id: Int): PartidoEntity? =
        partidoDao.getById(id)

    fun getByEquipo(idEquipo: Int): Flow<List<PartidoEntity>> =
        partidoDao.getByEquipo(idEquipo)

    fun getByCompeticionYTemporada(
        idCompeticion: Int,
        idTemporada: Int
    ): Flow<List<PartidoEntity>> =
        partidoDao.getByCompeticionYTemporada(idCompeticion, idTemporada)

    fun getByEstadio(idEstadio: Int): Flow<List<PartidoEntity>> = 
        partidoDao.getByEstadio(idEstadio)
    
    suspend fun insertPartido(partido: PartidoEntity): Long =
        partidoDao.insert(partido)

    suspend fun updatePartido(partido: PartidoEntity) =
        partidoDao.update(partido)

    suspend fun deletePartido(partido: PartidoEntity) =
        partidoDao.delete(partido)
}