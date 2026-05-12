package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Jugadores",
    foreignKeys = [
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
        ),
        ForeignKey(
            entity = EquipoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_equipo_actual"],
            onDelete = ForeignKey.SET_NULL,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("id_localidad"),
        Index("id_pais"),
        Index("id_equipo_actual")
    ]
)
data class JugadorEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val apellido1: String?,
    val apellido2: String?,
    @ColumnInfo(name = "fecha_nacimiento")
    val fechaNacimiento: String?,
    @ColumnInfo(name = "id_localidad")
    val idLocalidad: Int?,
    @ColumnInfo(name = "id_pais")
    val idPais: Int?,
    @ColumnInfo(name = "id_equipo_actual")
    val idEquipoActual: Int?,
    val posicion: String?,
    val activo: Boolean = true
)