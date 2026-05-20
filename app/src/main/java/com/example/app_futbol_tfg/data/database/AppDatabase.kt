package com.example.app_futbol_tfg.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.app_futbol_tfg.data.dao.ApiSyncDao
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
import com.example.app_futbol_tfg.data.dao.UsuarioLogroDao
import com.example.app_futbol_tfg.data.dao.UsuarioPartidoDao
import com.example.app_futbol_tfg.data.entity.ApiSyncEntity
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

// BBDD principal de la aplicación
// Room usa esta clase para generar automáticamente la estructura de tablas, relaciones y acceso
// a datos definidos en las entidades y los DAO
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
        UsuarioPartidoEntity::class,
        ApiSyncEntity::class
    ],
    // La versión se incrementa cada vez que el esquema de la BBDD cambie
    version = 5,
    // Este proyecto no exporta el esquema a ficheros JSON
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    // Métodos de acceso a datos (DAO) utilziados por la aplicación
    // Room genera automáticamente la implementación de cada DAO
    abstract fun competicionDao(): CompeticionDao
    abstract fun equipoDao(): EquipoDao
    abstract fun estadioDao(): EstadioDao
    abstract fun jugadorDao(): JugadorDao
    abstract fun localidadDao(): LocalidadDao
    abstract fun logroDao(): LogroDao
    abstract fun usuarioLogroDao(): UsuarioLogroDao
    abstract fun paisDao(): PaisDao
    abstract fun partidoDao(): PartidoDao
    abstract fun partidoJugadorDao(): PartidoJugadorDao
    abstract fun temporadaDao(): TemporadaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun usuarioPartidoDao(): UsuarioPartidoDao
    abstract fun apiSyncDao(): ApiSyncDao
}