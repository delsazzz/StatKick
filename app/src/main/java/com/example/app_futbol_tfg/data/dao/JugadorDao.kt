package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JugadorDao {

    @Query("SELECT * FROM Jugadores ORDER BY apellido1, nombre")
    fun getAll(): Flow<List<JugadorEntity>>
    
    @Query("SELECT * FROM Jugadores WHERE activo = 1 ORDER BY apellido1, nombre")
    fun getAllActivos(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE activo = 0 ORDER BY apellido1, nombre")
    fun getAllRetirados(): Flow<List<JugadorEntity>>
    // Room usa boolean como 1 y 0 porque SQLite no tiene tipo boolean real y por eso no pone True o False
    @Query("SELECT * FROM Jugadores WHERE id_equipo_actual = :idEquipo AND activo = 1 ORDER BY apellido1, nombre")
    fun getByEquipo(idEquipo: Int): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE id_equipo_actual IS NULL AND activo = 1")
    fun getSinEquipo(): Flow<List<JugadorEntity>>

    @Query("SELECT * FROM Jugadores WHERE id = :id")
    suspend fun getById(id: Int): JugadorEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(jugador: JugadorEntity): Long

    @Update
    suspend fun update(jugador: JugadorEntity)

    @Delete
    suspend fun delete(jugador: JugadorEntity)
}