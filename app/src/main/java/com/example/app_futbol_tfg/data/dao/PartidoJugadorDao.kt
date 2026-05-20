package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import kotlinx.coroutines.flow.Flow
import com.example.app_futbol_tfg.data.model.PlayerSeenStat
import com.example.app_futbol_tfg.data.model.CardStats

// DAO encargado de gestionar las estadísticas y relaciones entre partidos y jugadores
// Se utiliza para generar datos estadísticos mostrados en MatchDetail y StatsScreen
@Dao
interface PartidoJugadorDao {
    // Recupera todos los jugadores asociados a un partido concreto
    @Query("SELECT * FROM Partido_Jugador WHERE id_partido = :idPartido")
    fun getByPartido(idPartido: Int): Flow<List<PartidoJugadorEntity>>
    // Recupera jugadores pertenecientes a varios partidos simultáneamente
    @Query("SELECT * FROM Partido_Jugador WHERE id_partido IN (:ids)")
    fun getByPartidos(ids: List<Int>): Flow<List<PartidoJugadorEntity>>
    // Devuelve el historial de partidos asociados a un jugador
    @Query("""SELECT pj.* FROM Partido_Jugador pj INNER JOIN Partidos p ON p.id = pj.id_partido
            WHERE pj.id_jugador = :idJugador ORDER BY p.fecha DESC""")
    fun getByJugador(idJugador: Int): Flow<List<PartidoJugadorEntity>>
    // COALESCE evita valores nulos devolviendo 0 cuando no existen registros asociados
    // Estadísticas individuales acumuladas de jugadores
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
    // Estadísticas globales de jugadores visualizados por el usuario
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, e.escudo AS escudo, p.bandera AS bandera,
                COUNT(*) AS total FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                INNER JOIN Jugadores j ON j.id = pj.id_jugador LEFT JOIN Equipos e ON e.id = pj.id_equipo LEFT JOIN Pais p ON p.id = j.id_pais
                WHERE up.id_usuario = :idUsuario GROUP BY j.id, j.nombre, j.apellido1, e.escudo, p.bandera
                ORDER BY total DESC, j.nombre ASC LIMIT :limit""")
    fun getJugadoresMasVistos(idUsuario: Int, limit: Int = 10): Flow<List<PlayerSeenStat>>
    @Query("""SELECT COUNT(DISTINCT pj.id_jugador) FROM Usuario_Partido up INNER JOIN Partido_Jugador pj 
                ON pj.id_partido = up.id_partido WHERE up.id_usuario = :idUsuario""")
    fun countJugadoresDistintosVistos(idUsuario: Int): Flow<Int>
    // Rankings ofensivos generados a partir de los partidos registrados por el usuario
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, e.escudo AS escudo, p.bandera AS bandera, COALESCE(SUM(pj.goles), 0) AS total
                FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                INNER JOIN Jugadores j ON j.id = pj.id_jugador LEFT JOIN Equipos e ON e.id = pj.id_equipo LEFT JOIN Pais p ON p.id = j.id_pais
                WHERE up.id_usuario = :idUsuario GROUP BY j.id, j.nombre, j.apellido1, e.escudo, p.bandera HAVING total > 0
                ORDER BY total DESC, j.nombre ASC LIMIT :limit""")
    fun getTopGoleadoresVistos(idUsuario: Int, limit: Int = 10): Flow<List<PlayerSeenStat>>
    @Query(""" SELECT COALESCE(SUM(pj.goles), 0) FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                WHERE up.id_usuario = :idUsuario""")
    fun getTotalGolesVistos(idUsuario: Int): Flow<Int>
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, e.escudo AS escudo, p.bandera AS bandera, COALESCE(SUM(pj.asistencias), 0) AS total
                FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                INNER JOIN Jugadores j ON j.id = pj.id_jugador LEFT JOIN Equipos e ON e.id = pj.id_equipo LEFT JOIN Pais p ON p.id = j.id_pais
                WHERE up.id_usuario = :idUsuario GROUP BY j.id, j.nombre, j.apellido1, e.escudo, p.bandera HAVING total > 0
                ORDER BY total DESC, j.nombre ASC LIMIT :limit""")
    fun getTopAsistentesVistos(idUsuario: Int, limit: Int = 10): Flow<List<PlayerSeenStat>>
    @Query("""SELECT COALESCE(SUM(pj.asistencias), 0) FROM Usuario_Partido up INNER JOIN Partido_Jugador pj 
                ON pj.id_partido = up.id_partido WHERE up.id_usuario = :idUsuario""")
    fun getTotalAsistenciasVistas(idUsuario: Int): Flow<Int>
    // Estadísticas disciplinarias agregadas de tarjetas amarillas y rojas
    @Query("""SELECT COALESCE(SUM(pj.amarillas), 0) AS amarillas, COALESCE(SUM(pj.rojas), 0) AS rojas
                FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                WHERE up.id_usuario = :idUsuario""")
    fun getCardStats(idUsuario: Int): Flow<CardStats>
    // Rankings disciplinarios de jugadores con más tarjetas acumuladas
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, e.escudo AS escudo, p.bandera AS bandera, COALESCE(SUM(pj.amarillas), 0) AS total
                FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                INNER JOIN Jugadores j ON j.id = pj.id_jugador LEFT JOIN Equipos e ON e.id = pj.id_equipo LEFT JOIN Pais p ON p.id = j.id_pais
                WHERE up.id_usuario = :idUsuario GROUP BY j.id, j.nombre, j.apellido1, e.escudo, p.bandera HAVING total > 0
                ORDER BY total DESC, j.nombre ASC LIMIT :limit""")
    fun getTopAmarillasVistas(idUsuario: Int, limit: Int = 10): Flow<List<PlayerSeenStat>>
    @Query("""SELECT COALESCE(SUM(pj.amarillas), 0) FROM Usuario_Partido up
    INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido WHERE up.id_usuario = :idUsuario""")
    fun getTotalAmarillasVistas(idUsuario: Int): Flow<Int>
    @Query("""SELECT j.id AS id, j.nombre AS nombre, j.apellido1 AS apellido1, e.escudo AS escudo, p.bandera AS bandera, COALESCE(SUM(pj.rojas), 0) AS total
                FROM Usuario_Partido up INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido
                INNER JOIN Jugadores j ON j.id = pj.id_jugador LEFT JOIN Equipos e ON e.id = pj.id_equipo LEFT JOIN Pais p ON p.id = j.id_pais
                WHERE up.id_usuario = :idUsuario GROUP BY j.id, j.nombre, j.apellido1, e.escudo, p.bandera HAVING total > 0
                ORDER BY total DESC, j.nombre ASC LIMIT :limit""")
    fun getTopRojasVistas(idUsuario: Int, limit: Int = 10): Flow<List<PlayerSeenStat>>
    @Query("""SELECT COALESCE(SUM(pj.rojas), 0) FROM Usuario_Partido up 
                INNER JOIN Partido_Jugador pj ON pj.id_partido = up.id_partido WHERE up.id_usuario = :idUsuario""")
    fun getTotalRojasVistas(idUsuario: Int): Flow<Int>
    // Operaciones básicas de persistencia sobre Partido_Jugador
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(partidoJugador: PartidoJugadorEntity): Long
    @Update
    suspend fun update(partidoJugador: PartidoJugadorEntity)
    @Delete
    suspend fun delete(partidoJugador: PartidoJugadorEntity)
}