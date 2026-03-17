package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.PaisDao
import com.example.app_futbol_tfg.data.entity.PaisEntity
import kotlinx.coroutines.flow.Flow

class PaisRepository(
    private val paisDao: PaisDao
) {

    fun getAll(): Flow<List<PaisEntity>> = paisDao.getAll()

    suspend fun getPaisById(id: Int): PaisEntity? = paisDao.getById(id)

    suspend fun getByNombre(nombre: String): PaisEntity? = paisDao.getByNombre(nombre)
    
    suspend fun insertPais(pais: PaisEntity): Long = paisDao.insert(pais)

    suspend fun updatePais(pais: PaisEntity) = paisDao.update(pais)

    suspend fun deletePais(pais: PaisEntity) = paisDao.delete(pais)
}