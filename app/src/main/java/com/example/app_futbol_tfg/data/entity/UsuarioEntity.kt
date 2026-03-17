package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Index

@Entity(
    tableName = "Usuarios",
     indices = [
        // Estos índices con unique hacen que tanto nombre de usuario como email no se puedan repetir
        Index(value = ["nombre_usuario"], unique = true),
        Index(value = ["email"], unique = true)
     ]
)
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "nombre_usuario")
    val nombreUsuario: String,
    val email: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String
    val rol: String = "usuario"
)