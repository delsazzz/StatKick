package com.example.app_futbol_tfg.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Temporadas"
)
data class TemporadaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val temporada: String
)