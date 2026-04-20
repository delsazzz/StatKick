package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.PaisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaisDao {
    @Query("SELECT * FROM Pais ORDER BY nombre")
    fun getAll(): Flow<List<PaisEntity>>
    @Query("SELECT * FROM Pais WHERE id = :id")
    suspend fun getById(id: Int): PaisEntity?
    @Query("SELECT * FROM Pais WHERE nombre = :nombre LIMIT 1")
    suspend fun getByNombre(nombre: String): PaisEntity?
    @Query("SELECT COUNT(*) FROM Pais")
    suspend fun count(): Int
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(pais: PaisEntity): Long
    @Update
    suspend fun update(pais: PaisEntity)
    @Delete
    suspend fun delete(pais: PaisEntity)
}