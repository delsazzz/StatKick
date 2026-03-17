package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.LocalidadDao
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import kotlinx.coroutines.flow.Flow

class LocalidadRepository(
    private val localidadDao: LocalidadDao
) {

    fun getLocalidadesByPais(idPais: Int): Flow<List<LocalidadEntity>> =
        localidadDao.getByPais(idPais)

    suspend fun getLocalidadById(id: Int): LocalidadEntity? =
        localidadDao.getById(id)

    fun getAll(): Flow<List<LocalidadEntity>> = 
        localidadDao.getAll()

    suspend fun insertLocalidad(localidad: LocalidadEntity): Long =
        localidadDao.insert(localidad)

    suspend fun updateLocalidad(localidad: LocalidadEntity) =
        localidadDao.update(localidad)

    suspend fun deleteLocalidad(localidad: LocalidadEntity) =
        localidadDao.delete(localidad)
}