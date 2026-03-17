package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LogroDao {

    @Query("SELECT * FROM Logros ORDER BY nombre")
    fun getAll(): Flow<List<LogroEntity>>

    @Query("SELECT * FROM Logros WHERE id = :id")
    suspend fun getById(id: Int): LogroEntity?

    @Query(
        """
        SELECT l.* 
        FROM Logros l
        INNER JOIN Usuario_Logro ul ON ul.id_logro = l.id
        WHERE ul.id_usuario = :idUsuario
        ORDER BY l.nombre
        """
    )
    fun getLogrosByUsuario(idUsuario: Int): Flow<List<LogroEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLogro(logro: LogroEntity): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun asignarLogro(rel: UsuarioLogroEntity): Long
}