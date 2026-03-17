package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.CompeticionDao
import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import kotlinx.coroutines.flow.Flow

class CompeticionRepository(
    private val competicionDao: CompeticionDao
) {

    fun getAll(): Flow<List<CompeticionEntity>> =
        competicionDao.getAll()

    suspend fun getById(id: Int): CompeticionEntity? =
        competicionDao.getById(id)

    fun getByPais(idPais: Int): Flow<List<CompeticionEntity>> =
        competicionDao.getByPais(idPais)

    fun getSinPais(): Flow<List<CompeticionEntity>> = 
        competicionDao.getSinPais()

    suspend fun insertCompeticion(competicion: CompeticionEntity): Long =
        competicionDao.insert(competicion)

    suspend fun updateCompeticion(competicion: CompeticionEntity) =
        competicionDao.update(competicion)

    suspend fun deleteCompeticion(competicion: CompeticionEntity) =
        competicionDao.delete(competicion)
}