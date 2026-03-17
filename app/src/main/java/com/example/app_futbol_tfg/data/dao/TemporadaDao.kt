package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TemporadaDao {

    @Query("SELECT * FROM Temporadas ORDER BY temporada DESC")
    fun getAll(): Flow<List<TemporadaEntity>>

    @Query("SELECT * FROM Temporadas WHERE id = :id")
    suspend fun getById(id: Int): TemporadaEntity?

    @Query("SELECT * FROM Temporadas WHERE temporada = :temporada LIMIT 1")
    suspend fun getByTemporada(temporada: String): TemporadaEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(temporada: TemporadaEntity): Long
}