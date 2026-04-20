package com.example.app_futbol_tfg.data

import android.util.Log
import com.example.app_futbol_tfg.data.repository.ApiFootballRepository
import kotlinx.coroutines.delay

private const val TAG = "AppInitializer"

// IDs de las competiciones que queremos cargar en la app
val LIGAS_SELECCIONADAS = listOf(
    2,   // Champions League
    39,  // Premier League
    61,  // Ligue 1
    78,  // Bundesliga
    135, // Serie A
    140  // La Liga
)

// Temporadas que queremos cargar
val TEMPORADAS_SELECCIONADAS = listOf(2023, 2024)

// Se encarga de la carga inicial de datos desde la API al arrancar la app.
// Solo carga si Room está vacío para no gastar cuota innecesariamente.
// El orden respeta las foreign keys de la BBDD.
suspend fun initializeAppData(repo: ApiFootballRepository, apiKey: String) {
    try {
        // 1. Países — no depende de nadie, debe ir primero
        Log.d(TAG, "Iniciando carga de países...")
        repo.fetchAndSaveCountries(apiKey)

        // 2. Competiciones — con delay para respetar el límite de la API
        Log.d(TAG, "Iniciando carga de competiciones seleccionadas...")
        LIGAS_SELECCIONADAS.forEach { leagueId ->
            repo.fetchAndSaveLeagueById(apiKey, leagueId)
            delay(300)
        }

        // 3. Equipos y estadios — con delay para respetar el límite de la API
        Log.d(TAG, "Iniciando carga de equipos...")
        LIGAS_SELECCIONADAS.forEach { leagueId ->
            repo.fetchAndSaveTeams(apiKey, leagueId, 2024)
            delay(1500) // Esperamos 500ms entre cada llamada para no saturar la API
        }

        Log.d(TAG, "Carga inicial completada")
    } catch (e: Exception) {
        Log.e(TAG, "Error durante la carga inicial", e)
    }
}