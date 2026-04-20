package com.example.app_futbol_tfg.data.database

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log

// Este es el proveedor único de la BBDD
// Se aplica el patrón Singleton para garantizar una sola instancia de Room
object DatabaseProvider {
    private const val TAG = "DatabaseProvider"
    @Volatile
    private var INSTANCE: AppDatabase? = null
    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }
    }
    // Se crea la instancia de Room y lanza la carga inicial de datos en segundo plano
    private fun buildDatabase(context: Context): AppDatabase {
        try {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "futbol_tfg_database"
            )
                // Si cambia el esquema y no existe migración, Room recrea la BBDD
                .fallbackToDestructiveMigration()
                .build()
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    // Esto queda comentado porque los datos vienen desde API-Football ahora
                    // SeedData.seed(instance)
                } catch (e: SQLiteException) {
                    Log.e(TAG, "Error al ejecutar el seed de la base de datos", e)
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "Estado inválido durante el seed de la base de datos", e)
                }
            }
            return instance
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al crear la base de datos", e)
            throw e
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Error de estado al crear la base de datos", e)
            throw e
        }
    }
}