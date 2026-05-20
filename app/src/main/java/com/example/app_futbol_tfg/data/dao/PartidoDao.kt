package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import kotlinx.coroutines.flow.Flow

// DAO encargado de gestionar las operaciones relacionadas con los partidos almacenados
@Dao
interface PartidoDao {
    // Recupera todos los partidos ordenados cronológicamente
    @Query("SELECT * FROM Partidos ORDER BY fecha DESC")
    fun getAll(): Flow<List<PartidoEntity>>
    // Obtiene un partido concreto a partir de su identificador
    @Query("SELECT * FROM Partidos WHERE id = :id")
    suspend fun getById(id: Int): PartidoEntity?
    // Recupera partidos asociados a un equipo como local o visitante
    @Query(
        """
        SELECT * FROM Partidos
        WHERE id_equipo_local = :idEquipo OR id_equipo_visitante = :idEquipo
        ORDER BY fecha DESC
        """
    )
    fun getByEquipo(idEquipo: Int): Flow<List<PartidoEntity>>
    // Filtra partidos según competición y temporada
    @Query(
        """
        SELECT * FROM Partidos
        WHERE id_competicion = :idCompeticion AND id_temporada = :idTemporada
        ORDER BY fecha ASC
        """
    )
    fun getByCompeticionYTemporada(
        idCompeticion: Int,
        idTemporada: Int
    ): Flow<List<PartidoEntity>>
    // Recupera partidos disputados en un estadio concreto
    @Query("""
    SELECT * FROM Partidos
    WHERE id_estadio = :idEstadio
    ORDER BY fecha DESC
        """
    )
    fun getByEstadio(idEstadio: Int): Flow<List<PartidoEntity>>
    // Operaciones básicas de persistencia sobre partidos
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(partido: PartidoEntity): Long
    @Update
    suspend fun update(partido: PartidoEntity)
    @Delete
    suspend fun delete(partido: PartidoEntity)
}