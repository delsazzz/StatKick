package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Usuario_Partido",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PartidoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_partido"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuario", "id_partido"], unique = true),
        Index("id_usuario"),
        Index("id_partido")
    ]
)
data class UsuarioPartidoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,
    @ColumnInfo(name = "id_partido")
    val idPartido: Int,
    @ColumnInfo(name = "fecha_registro")
    val fechaRegistro: String
)