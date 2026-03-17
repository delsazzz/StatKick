package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.dao.TemporadaDao
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import kotlinx.coroutines.flow.Flow

class TemporadaRepository(
    private val temporadaDao: TemporadaDao
) {

    fun getAll(): Flow<List<TemporadaEntity>> = temporadaDao.getAll()

    suspend fun getById(id: Int): TemporadaEntity? = temporadaDao.getById(id)

    suspend fun getByTemporada(temporada: String): TemporadaEntity? =
        temporadaDao.getByTemporada(temporada)

    suspend fun insertTemporada(temporada: TemporadaEntity): Long =
        temporadaDao.insert(temporada)
}