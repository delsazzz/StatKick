package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstadioDao {
    @Query("SELECT * FROM Estadios ORDER BY nombre")
    fun getAll(): Flow<List<EstadioEntity>>
    @Query("SELECT * FROM Estadios WHERE id = :id")
    suspend fun getById(id: Int): EstadioEntity?
    @Query("SELECT * FROM Estadios WHERE id_pais = :idPais ORDER BY nombre")
    fun getByPais(idPais: Int): Flow<List<EstadioEntity>>
    @Query("SELECT * FROM Estadios WHERE id_localidad = :idLocalidad ORDER BY nombre")
    fun getByLocalidad(idLocalidad: Int): Flow<List<EstadioEntity>>

    @Query("""
    UPDATE Estadios 
    SET latitud = :latitud, longitud = :longitud, direccion = :direccion 
    WHERE nombre = :nombre
""")
    suspend fun updateCoordenadas(nombre: String, latitud: Double, longitud: Double, direccion: String?)
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(estadio: EstadioEntity): Long
    @Update
    suspend fun update(estadio: EstadioEntity)
    @Delete
    suspend fun delete(estadio: EstadioEntity)
}