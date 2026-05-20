package com.example.app_futbol_tfg.data.database

import android.content.Context
import android.database.sqlite.SQLiteException
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.util.Log
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Proveedor único de la BBDD
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
    // Crea la instancia de Room y lanza la carga inicial de datos en segundo plano
    private fun buildDatabase(context: Context): AppDatabase {
        return try {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "futbol_tfg_database"
            )
                // Si cambia el esquema y no existe migración, Room recrea la BBDD
                .fallbackToDestructiveMigration()
                .build()
            insertDemoUserIfNeeded(instance)
            instance
        } catch (e: SQLiteException) {
            Log.e(TAG, "Error al crear la base de datos", e)
            throw e
        } catch (e: IllegalStateException) {
            Log.e(TAG, "Error de estado al crear la base de datos", e)
            throw e
        }
    }
    // Inserta un usuario de prueba solo si todavía no existe
    // Los datos principales de fútbol se cargan desde API-Football
    private fun insertDemoUserIfNeeded(db: AppDatabase) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val usuarioExistente = db.usuarioDao().getById(2)
                    if (usuarioExistente == null) {
                        db.usuarioDao().insert(
                            UsuarioEntity(
                                id = 2,
                                nombreUsuario = "demo",
                                email = "demo@tfg.com",
                                passwordHash = "demo",
                                fechaRegistro = SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale.getDefault()
                                ).format(Date())
                            )
                        )
                    }
                } catch (e: SQLiteException) {
                    Log.e(TAG, "Error al ejecutar el seed de la base de datos", e)
                } catch (e: IllegalStateException) {
                    Log.e(TAG, "Estado inválido durante el seed de la base de datos", e)
                }
            }
    }
}