package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Partido_Jugador",
    foreignKeys = [
        ForeignKey(
            entity = PartidoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_partido"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = JugadorEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_jugador"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_equipo"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_partido", "id_jugador"], unique = true),
        Index("id_partido"),
        Index("id_jugador"),
        Index("id_equipo")
    ]
)
data class PartidoJugadorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_partido")
    val idPartido: Int,
    @ColumnInfo(name = "id_jugador")
    val idJugador: Int,
    @ColumnInfo(name = "id_equipo")
    val idEquipo: Int,
    val titular: Boolean = false,
    @ColumnInfo(name = "minutos_jugados")
    val minutosJugados: Int?,
    val goles: Int = 0,
    val asistencias: Int = 0,
    val amarillas: Int = 0,
    val rojas: Int = 0
)