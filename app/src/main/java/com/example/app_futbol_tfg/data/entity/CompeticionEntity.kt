package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// @Entity marca a Room que esa clase es una entidad para guardarla como tabla
@Entity(
    tableName = "Competiciones", // Nombre de la tabla
    foreignKeys = [
        ForeignKey(
            entity = PaisEntity::class, // Tabla relacionada de la FK
            parentColumns = ["id"], // Columna referencia de la FK
            childColumns = ["id_pais"], // Columna de esta tabla que referencia a la FK
            onDelete = ForeignKey.SET_NULL, // Si se elimina el país referenciado, el valor pasa a NULL
            onUpdate = ForeignKey.CASCADE // Si el id del país cambia, se actualiza automáticamente
        )
    ],
    indices = [Index("id_pais")] // Index crear un índice en la columna para mejorar rendimiento en búsquedas
)
// Se usa data class porque en Kotlin el objetivo de estas clases es almacenar datos
data class CompeticionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    @ColumnInfo(name = "id_pais") 
    val idPais: Int? // El ? define que esa variable o campo puede ser NULLABLE
)