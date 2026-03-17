package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.EstadioDao
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import kotlinx.coroutines.flow.Flow

class EstadioRepository(
    private val estadioDao: EstadioDao
) {

    fun getAll(): Flow<List<EstadioEntity>> = estadioDao.getAll()

    suspend fun getById(id: Int): EstadioEntity? = estadioDao.getById(id)

    fun getByPais(idPais: Int): Flow<List<EstadioEntity>> = estadioDao.getByPais(idPais)

    fun getByLocalidad(idLocalidad: Int): Flow<List<EstadioEntity>> = estadioDao.getByLocalidad(idLocalidad)
    
    suspend fun insertEstadio(estadio: EstadioEntity): Long =
        estadioDao.insert(estadio)

    suspend fun updateEstadio(estadio: EstadioEntity) =
        estadioDao.update(estadio)

    suspend fun deleteEstadio(estadio: EstadioEntity) =
        estadioDao.delete(estadio)
}