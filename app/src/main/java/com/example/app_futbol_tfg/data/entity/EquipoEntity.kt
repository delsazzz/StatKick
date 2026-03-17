package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Equipos",
    foreignKeys = [
        ForeignKey(
            entity = EstadioEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_estadio"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LocalidadEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_localidad"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PaisEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_pais"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_estadio"),
        Index("id_localidad"),
        Index("id_pais")
    ]
)
data class EquipoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    @ColumnInfo(name = "anio_fundacion")
    val anioFundacion: Int?,
    @ColumnInfo(name = "id_estadio")
    val idEstadio: Int?,
    @ColumnInfo(name = "id_localidad")
    val idLocalidad: Int?,
    @ColumnInfo(name = "id_pais")
    val idPais: Int?
)