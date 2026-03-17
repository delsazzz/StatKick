package com.example.app_futbol_tfg.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "Usuario_Logro",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LogroEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_logro"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuario", "id_logro"], unique = true),
        Index("id_usuario"),
        Index("id_logro")
    ]
)
data class UsuarioLogroEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "id_usuario")
    val idUsuario: Int,
    @ColumnInfo(name = "id_logro")
    val idLogro: Int,
    @ColumnInfo(name = "fecha_obtenido")
    val fechaObtenido: String
)