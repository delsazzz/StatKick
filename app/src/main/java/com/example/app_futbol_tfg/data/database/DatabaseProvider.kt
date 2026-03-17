package com.example.app_futbol_tfg.data.database

import android.content.Context
import androidx.room.Room
// Creamos una clase singleton automática, solo puede haber una instancia
object DatabaseProvider {

    @Volatile
    // La instancia se crea en esta variable
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            // Aquí Room crea una BBDD con ese nombre y genera el SQLite real
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "futbol_tfg_database"
            )
                // Si el esquema de la BBDD cambiara y la versión no coincide, Room borra la BBDD y la recrea
                .fallbackToDestructiveMigration()
                .build()

            INSTANCE = instance
            instance
        }
    }
}