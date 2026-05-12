package com.example.app_futbol_tfg.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app_futbol_tfg.data.dao.CompeticionDao
import com.example.app_futbol_tfg.data.dao.EquipoDao
import com.example.app_futbol_tfg.data.dao.EstadioDao
import com.example.app_futbol_tfg.data.dao.JugadorDao
import com.example.app_futbol_tfg.data.dao.LocalidadDao
import com.example.app_futbol_tfg.data.dao.LogroDao
import com.example.app_futbol_tfg.data.dao.PaisDao
import com.example.app_futbol_tfg.data.dao.PartidoDao
import com.example.app_futbol_tfg.data.dao.PartidoJugadorDao
import com.example.app_futbol_tfg.data.dao.TemporadaDao
import com.example.app_futbol_tfg.data.dao.UsuarioDao
import com.example.app_futbol_tfg.data.dao.UsuarioPartidoDao
import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.PaisEntity
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity

// Esta es la BBDD principal de la app
// Aquí se registran las entidades que Room debe convertir en tablas
@Database(
    entities = [
        CompeticionEntity::class,
        EquipoEntity::class,
        EstadioEntity::class,
        JugadorEntity::class,
        LocalidadEntity::class,
        LogroEntity::class,
        PaisEntity::class,
        PartidoEntity::class,
        PartidoJugadorEntity::class,
        TemporadaEntity::class,
        UsuarioEntity::class,
        UsuarioLogroEntity::class,
        UsuarioPartidoEntity::class
    ],
    // La versión debe incrementarse cada vez que el esquema de la BBDD cambie
    version = 2,
    // Este proyecto no exporta el esquema a ficheros JSON
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    // Todos estos métodos son accesibles para Room
    // Genera automáticamente la implementación de cada DAO
    abstract fun competicionDao(): CompeticionDao
    abstract fun equipoDao(): EquipoDao
    abstract fun estadioDao(): EstadioDao
    abstract fun jugadorDao(): JugadorDao
    abstract fun localidadDao(): LocalidadDao
    abstract fun logroDao(): LogroDao
    abstract fun paisDao(): PaisDao
    abstract fun partidoDao(): PartidoDao
    abstract fun partidoJugadorDao(): PartidoJugadorDao
    abstract fun temporadaDao(): TemporadaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun usuarioPartidoDao(): UsuarioPartidoDao
}