package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import com.example.app_futbol_tfg.ui.screens.matchdetail.JugadorPartidoDetalle
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
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, j.apellido2 AS apellido2,
        pj.id_equipo AS idEquipo, pj.titular AS titular, pj.minutos_jugados AS minutos_jugados 
        FROM Jugadores j INNER JOIN Partido_Jugador pj ON j.id = pj.id_jugador
        WHERE pj.id_partido = :idPartido ORDER BY pj.id_equipo, pj.titular DESC, j.apellido1, j.nombre""")
    fun getDetalleByPartido(idPartido: Int): Flow<List<JugadorPartidoDetalle>>
    @Query("SELECT * FROM Jugadores WHERE id = :id")
    suspend fun getById(id: Int): JugadorEntity?
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(jugador: JugadorEntity): Long
    @Update
    suspend fun update(jugador: JugadorEntity)
    @Delete
    suspend fun delete(jugador: JugadorEntity)
}