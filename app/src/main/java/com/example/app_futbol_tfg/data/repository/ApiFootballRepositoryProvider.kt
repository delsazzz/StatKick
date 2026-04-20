package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.remote.api.ApiProvider

// Proveedor singleton del ApiFootballRepository.
// Garantiza que toda la app use la misma instancia del repositorio.
object ApiFootballRepositoryProvider {

    @Volatile
    private var INSTANCE: ApiFootballRepository? = null

    fun getInstance(db: AppDatabase): ApiFootballRepository {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: ApiFootballRepository(
                api = ApiProvider.apiFootballService,
                db = db
            ).also { INSTANCE = it }
        }
    }
}