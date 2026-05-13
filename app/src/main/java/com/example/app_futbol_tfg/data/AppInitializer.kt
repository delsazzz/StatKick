package com.example.app_futbol_tfg.data

import android.util.Log
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.repository.ApiFootballRepository
import kotlinx.coroutines.delay
import com.example.app_futbol_tfg.data.entity.LogroEntity

private const val TAG = "AppInitializer"

// IDs de las competiciones que queremos cargar en la app
val LIGAS_SELECCIONADAS = listOf(
    2,   // Champions League
    140  // La Liga
)

// Temporadas que queremos cargar
val TEMPORADAS_SELECCIONADAS = listOf(2023, 2024)

// Se encarga de la carga inicial de datos desde la API al arrancar la app.
// Solo carga si Room está vacío para no gastar cuota innecesariamente.
// El orden respeta las foreign keys de la BBDD.
suspend fun initializeAppData(repo: ApiFootballRepository, apiKey: String, database: AppDatabase) {
    try {
        seedLogros(database)
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
private suspend fun seedLogros(database: AppDatabase) {
    val logroDao = database.logroDao()

    logroDao.insertLogro(LogroEntity(id = 1, nombre = "Primer partido registrado", descripcion = "Has añadido tu primer partido a la aplicación."))
    logroDao.insertLogro(LogroEntity(id = 2, nombre = "Aficionado en marcha", descripcion = "Has registrado al menos 5 partidos."))
    logroDao.insertLogro(LogroEntity(id = 3, nombre = "Veterano de grada", descripcion = "Has registrado al menos 10 partidos."))
    logroDao.insertLogro(LogroEntity(id = 4, nombre = "Fan del gol", descripcion = "Has visto al menos 25 goles."))
    logroDao.insertLogro(LogroEntity(id = 5, nombre = "Lluvia de goles", descripcion = "Has visto al menos 50 goles."))
    logroDao.insertLogro(LogroEntity(id = 6, nombre = "Coleccionista de equipos", descripcion = "Has visto al menos 5 equipos diferentes."))
    logroDao.insertLogro(LogroEntity(id = 7, nombre = "Plantilla conocida", descripcion = "Has visto al menos 10 jugadores diferentes."))
    logroDao.insertLogro(LogroEntity(id = 8, nombre = "Explorador de estadios", descripcion = "Has visitado al menos 3 estadios diferentes."))
    logroDao.insertLogro(LogroEntity(id = 9, nombre = "Ruta internacional", descripcion = "Has registrado partidos de varias competiciones."))
    logroDao.insertLogro(LogroEntity(id = 10, nombre = "Partido igualado", descripcion = "Has visto al menos un empate."))
}