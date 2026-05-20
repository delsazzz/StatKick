package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Entidad que representa los partidos almacenados en la base de datos local
// Mantiene relaciones con equipos, competición, temporada y estadio
@Entity(
    tableName = "Partidos",
    // Relaciones de integridad referencial entre partidos y el resto de entidades principales
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
    // Índices utilizados para optimizar búsquedas y relaciones frecuentes
    indices = [
        Index("id_equipo_local"),
        Index("id_equipo_visitante"),
        Index("id_temporada"),
        Index("id_competicion"),
        Index("id_estadio")
    ]
)
// Modelo persistente utilizado por Room para representar partidos
data class PartidoEntity(
    // Identificador único del partido
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