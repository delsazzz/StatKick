package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Partidos",
    foreignKeys = [
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_equipo_local"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_equipo_visitante"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TemporadaEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_temporada"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CompeticionEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_competicion"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EstadioEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_estadio"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_equipo_local"),
        Index("id_equipo_visitante"),
        Index("id_temporada"),
        Index("id_competicion"),
        Index("id_estadio")
    ]
)
data class PartidoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_equipo_local")
    val idEquipoLocal: Int,
    @ColumnInfo(name = "id_equipo_visitante")
    val idEquipoVisitante: Int,
    @ColumnInfo(name = "goles_local")
    val golesLocal: Int,
    @ColumnInfo(name = "goles_visitante")
    val golesVisitante: Int,
    val fecha: String,
    @ColumnInfo(name = "id_temporada")
    val idTemporada: Int?,
    @ColumnInfo(name = "id_competicion")
    val idCompeticion: Int?,
    @ColumnInfo(name = "id_estadio")
    val idEstadio: Int?,
    val jornada: String?
)