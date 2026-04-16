package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalidadDao {
    @Query("SELECT * FROM Localidad WHERE id_pais = :idPais ORDER BY nombre")
    fun getByPais(idPais: Int): Flow<List<LocalidadEntity>>
    @Query("SELECT * FROM Localidad WHERE id = :id")
    suspend fun getById(id: Int): LocalidadEntity?
    @Query("SELECT * FROM Localidad ORDER BY nombre")
    fun getAll(): Flow<List<LocalidadEntity>>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(localidad: LocalidadEntity): Long
    @Update
    suspend fun update(localidad: LocalidadEntity)
    @Delete
    suspend fun delete(localidad: LocalidadEntity)
}