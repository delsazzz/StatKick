package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.EquipoDao
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import kotlinx.coroutines.flow.Flow

class EquipoRepository(
    private val equipoDao: EquipoDao
) {

    fun getAll(): Flow<List<EquipoEntity>> = equipoDao.getAll()

    suspend fun getById(id: Int): EquipoEntity? = equipoDao.getById(id)

    fun getByPais(idPais: Int): Flow<List<EquipoEntity>> =
        equipoDao.getByPais(idPais)

    fun getSinPais(): Flow<List<EquipoEntity>> = equipoDao.getSinPais()
    
        suspend fun insertEquipo(equipo: EquipoEntity): Long =
        equipoDao.insert(equipo)

    suspend fun updateEquipo(equipo: EquipoEntity) =
        equipoDao.update(equipo)

    suspend fun deleteEquipo(equipo: EquipoEntity) =
        equipoDao.delete(equipo)
}