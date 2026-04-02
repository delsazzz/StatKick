package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioPartidoDao {

    @Query(
        """
        SELECT p.* FROM Partidos p
        INNER JOIN Usuario_Partido up ON up.id_partido = p.id
        WHERE up.id_usuario = :idUsuario
        ORDER BY p.fecha DESC
        """
    )
    fun getPartidosByUsuario(idUsuario: Int): Flow<List<PartidoEntity>>

    @Query("SELECT COUNT(*) FROM Usuario_Partido WHERE id_usuario = :idUsuario")
    suspend fun countByUsuario(idUsuario: Int): Int

    @Query(
        """
        SELECT * FROM Usuario_Partido
        WHERE id_usuario = :idUsuario AND id_partido = :idPartido
        LIMIT 1
        """
    )
    suspend fun getRelacion(idUsuario: Int, idPartido: Int): UsuarioPartidoEntity?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuarioPartido: UsuarioPartidoEntity): Long

    @Delete
    suspend fun delete(relacion: UsuarioPartidoEntity)
}