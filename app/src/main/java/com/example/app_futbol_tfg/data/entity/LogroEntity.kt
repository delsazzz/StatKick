package com.example.app_futbol_tfg.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Logros"
)
data class LogroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val descripcion: String?
)