package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {

    @Query("SELECT * FROM Equipos ORDER BY nombre")
    fun getAll(): Flow<List<EquipoEntity>>

    @Query("SELECT * FROM Equipos WHERE id = :id")
    suspend fun getById(id: Int): EquipoEntity?

    @Query("SELECT * FROM Equipos WHERE id_pais = :idPais ORDER BY nombre")
    fun getByPais(idPais: Int): Flow<List<EquipoEntity>>

    @Query("SELECT * FROM Equipos WHERE id_pais IS NULL ORDER BY nombre")
    fun getSinPais(): Flow<List<EquipoEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(equipo: EquipoEntity): Long

    @Update
    suspend fun update(equipo: EquipoEntity)

    @Delete
    suspend fun delete(equipo: EquipoEntity)
}