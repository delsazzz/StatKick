package com.example.app_futbol_tfg.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Pais"
)
data class PaisEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val bandera: String? = null
)