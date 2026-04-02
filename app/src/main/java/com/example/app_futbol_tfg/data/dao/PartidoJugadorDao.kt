package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PartidoJugadorDao {

    @Query("SELECT * FROM Partido_Jugador WHERE id_partido = :idPartido")
    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>>

    @Query("SELECT * FROM Partido_Jugador WHERE id_partido IN (:ids)")
    fun getByPartidos(ids: List<Int>): Flow<List<PartidoJugadorEntity>>

    @Query(
        """
        SELECT pj.*
        FROM Partido_Jugador pj
        INNER JOIN Partidos p ON p.id = pj.id_partido
        WHERE pj.id_jugador = :idJugador
        ORDER BY p.fecha DESC
        """
    )
    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>>
    // COALESCE lo que hace es devolver 0 si es NULL para poder hacer mejor las operaciones
    @Query("SELECT COALESCE(SUM(goles), 0) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalGoles(idJugador: Int): Int

    @Query("SELECT COALESCE(SUM(asistencias), 0) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalAsistencias(idJugador: Int): Int

    @Query("SELECT COALESCE(SUM(amarillas), 0) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalAmarillas(idJugador: Int): Int

    @Query("SELECT COALESCE(SUM(rojas), 0) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalRojas(idJugador: Int): Int

    @Query("SELECT COALESCE(SUM(minutos_jugados), 0) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalMinutos(idJugador: Int): Int

    @Query("SELECT COUNT(*) FROM Partido_Jugador WHERE id_jugador = :idJugador")
    suspend fun getTotalPartidos(idJugador: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(partidoJugador: PartidoJugadorEntity): Long

    @Update
    suspend fun update(partidoJugador: PartidoJugadorEntity)

    @Delete
    suspend fun delete(partidoJugador: PartidoJugadorEntity)
}