package com.example.app_futbol_tfg.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.app_futbol_tfg.data.entity.UsuarioEntity

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM Usuarios WHERE id = :id")
    suspend fun getById(id: Int): UsuarioEntity?
    @Query("SELECT * FROM Usuarios WHERE nombre_usuario = :nombreUsuario LIMIT 1")
    suspend fun getByUsername(nombreUsuario: String): UsuarioEntity?
    @Query("SELECT * FROM Usuarios WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UsuarioEntity?
    // En este caso ABORT hace que si hay conflicto con una restricción en la base de datos
    // es decir un email o nombre de usuario repetido la operación se cancela y se lanza una excepción
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(usuario: UsuarioEntity): Long
    @Update
    suspend fun update(usuario: UsuarioEntity)
    @Delete
    suspend fun delete(usuario: UsuarioEntity)
}