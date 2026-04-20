package com.example.app_futbol_tfg.data.repository

import android.util.Log
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.mapper.toCompeticionEntity
import com.example.app_futbol_tfg.data.mapper.toEquipoEntity
import com.example.app_futbol_tfg.data.mapper.toEstadioEntity
import com.example.app_futbol_tfg.data.mapper.toJugadorEntity
import com.example.app_futbol_tfg.data.mapper.toPaisEntity
import com.example.app_futbol_tfg.data.mapper.toPartidoEntity
import com.example.app_futbol_tfg.data.remote.api.ApiFootballService

private const val TAG = "ApiFootballRepository"

// Repositorio que gestiona la comunicación con API-Football
// y el guardado de los datos en Room como caché local.
// Implementa el patrón offline-first: la API alimenta Room,
// y la UI siempre lee desde Room.
class ApiFootballRepository(
    private val api: ApiFootballService,
    private val db: AppDatabase
) {

    // Obtiene los partidos de una liga y temporada desde la API
    // y los guarda en Room. Devuelve true si tuvo éxito.
    suspend fun fetchAndSaveFixtures(
        apiKey: String,
        leagueId: Int,
        season: Int
    ): Boolean {
        return try {
            val response = api.getFixtures(apiKey, leagueId, season)
            if (response.isSuccessful) {
                val fixtures = response.body()?.response ?: emptyList()
                fixtures.forEach { fixture ->
                    db.partidoDao().insert(fixture.toPartidoEntity())
                }
                Log.d(TAG, "Partidos guardados en Room: ${fixtures.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener partidos: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener partidos de la API", e)
            false
        }
    }

    // Obtiene las competiciones desde la API y las guarda en Room
    suspend fun fetchAndSaveLeagues(apiKey: String): Boolean {
        return try {
            val response = api.getLeagues(apiKey)
            if (response.isSuccessful) {
                val leagues = response.body()?.response ?: emptyList()
                leagues.forEach { league ->
                    db.competicionDao().insert(league.toCompeticionEntity())
                }
                Log.d(TAG, "Competiciones guardadas en Room: ${leagues.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener competiciones: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener competiciones de la API", e)
            false
        }
    }

    // Obtiene los equipos de una liga y temporada desde la API
    // y guarda tanto el equipo como su estadio en Room
    suspend fun fetchAndSaveTeams(
        apiKey: String,
        leagueId: Int,
        season: Int
    ): Boolean {
        return try {
            val response = api.getTeams(apiKey, leagueId, season)
            if (response.isSuccessful) {
                val teams = response.body()?.response ?: emptyList()
                teams.forEach { team ->
                    team.toEstadioEntity()?.let { estadio ->
                        db.estadioDao().insert(estadio)
                    }
                    val paisEntity = team.venue?.country?.let { nombrePais ->
                        db.paisDao().getByNombre(nombrePais)
                    }
                    db.equipoDao().insert(
                        team.toEquipoEntity().copy(idPais = paisEntity?.id)
                    )
                }
                Log.d(TAG, "Equipos guardados en Room: ${teams.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener equipos: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener equipos de la API", e)
            false
        }
    }

    // Obtiene los jugadores de un equipo y temporada desde la API
    // y los guarda en Room asignándoles el equipo correspondiente
    suspend fun fetchAndSavePlayers(
        apiKey: String,
        teamId: Int,
        season: Int
    ): Boolean {
        return try {
            val response = api.getPlayers(apiKey, teamId, season)
            if (response.isSuccessful) {
                val players = response.body()?.response ?: emptyList()
                players.forEach { player ->
                    db.jugadorDao().insert(
                        // Asignamos el equipo al jugador antes de guardarlo
                        player.toJugadorEntity().copy(idEquipoActual = teamId)
                    )
                }
                Log.d(TAG, "Jugadores guardados en Room: ${players.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener jugadores: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener jugadores de la API", e)
            false
        }
    }
    // Obtiene los países desde la API y los guarda en Room.
    // Si ya existen datos no hace la llamada para ahorrar cuota.
    suspend fun fetchAndSaveCountries(apiKey: String): Boolean {
        return try {
            val existentes = db.paisDao().count()
            if (existentes > 0) {
                Log.d(TAG, "Países ya en Room ($existentes), omitiendo llamada a la API")
                return true
            }
            val response = api.getCountries(apiKey)
            if (response.isSuccessful) {
                val countries = response.body()?.response ?: emptyList()
                countries.forEach { country ->
                    db.paisDao().insert(country.toPaisEntity())
                }
                Log.d(TAG, "Países guardados en Room: ${countries.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener países: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener países de la API", e)
            false
        }
    }
    // Obtiene una competición concreta por su id desde la API y la guarda en Room.
// Se usa en la carga inicial para cargar solo las ligas seleccionadas.
    suspend fun fetchAndSaveLeagueById(apiKey: String, leagueId: Int): Boolean {
        return try {
            val response = api.getLeagues(apiKey, leagueId)
            if (response.isSuccessful) {
                val leagues = response.body()?.response ?: emptyList()
                leagues.forEach { league ->
                    db.competicionDao().insert(league.toCompeticionEntity())
                }
                Log.d(TAG, "Competición $leagueId guardada en Room")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener competición $leagueId: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener competición $leagueId", e)
            false
        }
    }
}