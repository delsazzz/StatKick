package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity
import kotlinx.coroutines.flow.Flow
import com.example.app_futbol_tfg.data.model.TeamSeenStat
import com.example.app_futbol_tfg.data.model.StadiumSeenStat

@Dao
interface UsuarioPartidoDao {

    @Query("""SELECT p.* FROM Partidos p
              INNER JOIN Usuario_Partido up ON up.id_partido = p.id
              WHERE up.id_usuario = :idUsuario
              ORDER BY p.fecha DESC""")
    fun getPartidosByUsuario(idUsuario: Int): Flow<List<PartidoEntity>>

    @Query("SELECT COUNT(*) FROM Usuario_Partido WHERE id_usuario = :idUsuario")
    suspend fun countByUsuario(idUsuario: Int): Int

    @Query("""SELECT * FROM Usuario_Partido
              WHERE id_usuario = :idUsuario AND id_partido = :idPartido
              LIMIT 1""")
    suspend fun getRelacion(idUsuario: Int, idPartido: Int): UsuarioPartidoEntity?

    @Query("DELETE FROM Usuario_Partido WHERE id_usuario = :userId AND id_partido = :matchId")
    suspend fun deleteRelacion(userId: Int, matchId: Int)

    @Query("SELECT COUNT(*) FROM Usuario_Partido WHERE id_usuario = :idUsuario")
    fun countByUsuarioFlow(idUsuario: Int): Flow<Int>

    @Query("""SELECT e.id AS id, e.nombre AS nombre, e.escudo AS escudo, COUNT(*) AS vecesVisto
              FROM (
                  SELECT p.id_equipo_local AS id_equipo
                  FROM Usuario_Partido up INNER JOIN Partidos p ON p.id = up.id_partido
                  WHERE up.id_usuario = :idUsuario
                  UNION ALL
                  SELECT p.id_equipo_visitante AS id_equipo
                  FROM Usuario_Partido up INNER JOIN Partidos p ON p.id = up.id_partido
                  WHERE up.id_usuario = :idUsuario
              ) equipos_usuario
              INNER JOIN Equipos e ON e.id = equipos_usuario.id_equipo
              GROUP BY e.id, e.nombre, e.escudo
              ORDER BY vecesVisto DESC, e.nombre ASC
              LIMIT :limit""")
    fun getTopEquiposVistos(idUsuario: Int, limit: Int = 10): Flow<List<TeamSeenStat>>

    @Query("""SELECT COUNT(DISTINCT id_equipo) FROM (
                  SELECT p.id_equipo_local AS id_equipo
                  FROM Usuario_Partido up INNER JOIN Partidos p ON p.id = up.id_partido
                  WHERE up.id_usuario = :idUsuario
                  UNION
                  SELECT p.id_equipo_visitante AS id_equipo
                  FROM Usuario_Partido up INNER JOIN Partidos p ON p.id = up.id_partido
                  WHERE up.id_usuario = :idUsuario
              )""")
    fun countEquiposDistintosVistos(idUsuario: Int): Flow<Int>

    @Query("""SELECT es.id AS id, es.nombre AS nombre, COUNT(*) AS total
              FROM Usuario_Partido up
              INNER JOIN Partidos p ON p.id = up.id_partido
              INNER JOIN Estadios es ON es.id = p.id_estadio
              WHERE up.id_usuario = :idUsuario AND p.id_estadio IS NOT NULL
              GROUP BY es.id, es.nombre
              ORDER BY total DESC, es.nombre ASC
              LIMIT :limit""")
    fun getEstadiosVistos(idUsuario: Int, limit: Int = 10): Flow<List<StadiumSeenStat>>

    @Query("""SELECT COUNT(DISTINCT p.id_estadio)
              FROM Usuario_Partido up
              INNER JOIN Partidos p ON p.id = up.id_partido
              WHERE up.id_usuario = :idUsuario AND p.id_estadio IS NOT NULL""")
    fun countEstadiosDistintosVistos(idUsuario: Int): Flow<Int>

    // ── Nueva query: cuenta cuántos partidos ha visto el usuario en un estadio concreto
    // A diferencia de countByEstadio en PartidoDao (que cuenta todos los partidos del estadio),
    // esta cruza con Usuario_Partido para contar solo los del usuario logueado
    @Query("""SELECT COUNT(*) FROM Usuario_Partido up
              INNER JOIN Partidos p ON p.id = up.id_partido
              WHERE up.id_usuario = :idUsuario AND p.id_estadio = :idEstadio""")
    fun countPartidosEnEstadio(idUsuario: Int, idEstadio: Int): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuarioPartido: UsuarioPartidoEntity): Long

    @Delete
    suspend fun delete(relacion: UsuarioPartidoEntity)
}