package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Localidad",
    foreignKeys = [
        ForeignKey(
            entity = PaisEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_pais"],
            onDelete = ForeignKey.RESTRICT, // Con RESTRICT si una localidad usa un país este no puede ser borrado
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_pais")
    ]
)
data class LocalidadEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    @ColumnInfo(name = "id_pais")
    val idPais: Int
)