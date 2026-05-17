package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioLogroDao {

    @Query("SELECT * FROM Usuario_Logro WHERE id_usuario = :idUsuario")
    fun getByUsuario(idUsuario: Int): Flow<List<UsuarioLogroEntity>>

    @Query("""
        SELECT EXISTS(
            SELECT 1 
            FROM Usuario_Logro 
            WHERE id_usuario = :idUsuario 
            AND id_logro = :idLogro
        )
    """)
    suspend fun existeLogroUsuario(idUsuario: Int, idLogro: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(usuarioLogro: UsuarioLogroEntity)
}