package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM Usuarios WHERE id = :id")
    suspend fun getById(id: Int): UsuarioEntity?
    @Query("SELECT * FROM Usuarios WHERE nombre_usuario = :nombreUsuario LIMIT 1")
    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity?
    @Query("SELECT * FROM Usuarios WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UsuarioEntity?
    @Query("SELECT * FROM Usuarios WHERE id = :id LIMIT 1")
    fun getByIdFlow(id: Int): Flow<UsuarioEntity?>
    // En este caso ABORT hace que si hay conflicto con una restricción en la base de datos
    // es decir un email o nombre de usuario repetido la operación se cancela y se lanza una excepción
    @Query("""SELECT EXISTS(SELECT 1 FROM Usuarios WHERE nombre_usuario = :nombreUsuario AND id != :userId)""")
    suspend fun existeNombreUsuarioEnOtroUsuario(nombreUsuario: String, userId: Int): Boolean
    @Query("""UPDATE Usuarios SET nombre_usuario = :nuevoNombre, avatar = :nuevoAvatar WHERE id = :userId""")
    suspend fun actualizarPerfil(userId: Int, nuevoNombre: String, nuevoAvatar: String)
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: UsuarioEntity): Long
    @Update
    suspend fun update(usuario: UsuarioEntity)
    @Delete
    suspend fun delete(usuario: UsuarioEntity)
}