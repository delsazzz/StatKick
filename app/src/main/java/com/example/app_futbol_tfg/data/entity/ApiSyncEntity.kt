package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

// Registra qué consultas se han hecho ya a la API
// para evitar repetirlas y gastar cuota innecesariamente.
// La clave primaria compuesta es tipo + id (ej: "team" + 33, "league" + 140)
@Entity(
    tableName = "Api_Sync",
    primaryKeys = ["tipo", "id_externo"]
)
data class ApiSyncEntity(
    val tipo: String, // "team" o "league"
    @ColumnInfo(name = "id_externo")
    val idExterno: Int,
    @ColumnInfo(name = "ultima_sync")
    val ultimaSync: String // fecha en formato YYYY-MM-DD
)