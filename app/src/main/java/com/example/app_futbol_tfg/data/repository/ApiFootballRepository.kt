package com.example.app_futbol_tfg.data.repository

import android.util.Log
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.entity.ApiSyncEntity
import com.example.app_futbol_tfg.data.mapper.toCompeticionEntity
import com.example.app_futbol_tfg.data.mapper.toEquipoEntity
import com.example.app_futbol_tfg.data.mapper.toEstadioEntity
import com.example.app_futbol_tfg.data.mapper.toJugadorEntity
import com.example.app_futbol_tfg.data.mapper.toPaisEntity
import com.example.app_futbol_tfg.data.mapper.toPartidoEntity
import com.example.app_futbol_tfg.data.mapper.toPartidoJugadorEntity
import com.example.app_futbol_tfg.data.remote.api.ApiFootballService
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.app_futbol_tfg.BuildConfig
import com.example.app_futbol_tfg.data.entity.TemporadaEntity

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
    // Obtiene los partidos de una liga y temporada desde la API  y los guarda en Room. Solo llama a la API si no hay partidos
    // de esa liga ya en Room para ahorrar cuota.
    suspend fun fetchAndSaveFixturesByLeague(
        apiKey: String,
        leagueId: Int,
        season: Int
    ): Boolean {
        return try {
            val yaSincronizado = db.apiSyncDao().hasSynced("league", leagueId) > 0
            if (yaSincronizado) {
                Log.d(TAG, "Partidos de liga $leagueId ya sincronizados, omitiendo llamada")
                return true
            }
            val response = api.getFixtures(apiKey, leagueId = leagueId, season = season)
            if (response.isSuccessful) {
                val fixtures = response.body()?.response ?: emptyList()
                val finalizados = fixtures.filter { it.fixture.status?.short == "FT" }
                finalizados.forEach { fixture ->
                    try {
                        // Buscamos o creamos la temporada en Room
                        val seasonYear = fixture.league.season?.toString() ?: "Desconocida"
                        var temporada = db.temporadaDao().getByTemporada(seasonYear)
                        if (temporada == null) {
                            val newId = db.temporadaDao().insert(
                                TemporadaEntity(temporada = seasonYear)
                            )
                            temporada = TemporadaEntity(id = newId.toInt(), temporada = seasonYear)
                        }
                        // Insertamos el partido con la temporada correcta
                        db.partidoDao().insert(
                            fixture.toPartidoEntity().copy(idTemporada = temporada.id)
                        )
                    } catch (e: android.database.sqlite.SQLiteConstraintException) {
                        Log.w(TAG, "Partido ${fixture.fixture.id} ignorado por FK inexistente")
                    }
                }
                // Registramos que ya hemos consultado esta liga
                db.apiSyncDao().markAsSynced(
                    ApiSyncEntity(
                        tipo = "league",
                        idExterno = leagueId,
                        ultimaSync = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    )
                )
                Log.d(TAG, "Partidos de liga $leagueId guardados en Room: ${finalizados.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener partidos liga $leagueId: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener partidos liga $leagueId", e)
            false
        }
    }
    // Obtiene los partidos de un equipo concreto desde la API y los guarda en Room. Solo llama a la API si no hay partidos
    // de ese equipo ya en Room para ahorrar cuota.
    suspend fun fetchAndSaveFixturesByTeam(
        apiKey: String,
        teamId: Int,
        season: Int
    ): Boolean {
        return try {
            val yaSincronizado = db.apiSyncDao().hasSynced("team", teamId) > 0
            if (yaSincronizado) {
                Log.d(TAG, "Partidos del equipo $teamId ya sincronizados, omitiendo llamada")
                return true
            }
            val response = api.getFixtures(apiKey, teamId = teamId, season = season)
            if (response.isSuccessful) {
                val fixtures = response.body()?.response ?: emptyList()
                val finalizados = fixtures.filter { it.fixture.status?.short == "FT" }
                finalizados.forEach { fixture ->
                    try {
                        // Buscamos o creamos la temporada en Room
                        val seasonYear = fixture.league.season?.toString() ?: "Desconocida"
                        var temporada = db.temporadaDao().getByTemporada(seasonYear)
                        if (temporada == null) {
                            val newId = db.temporadaDao().insert(
                                TemporadaEntity(temporada = seasonYear)
                            )
                            temporada = TemporadaEntity(id = newId.toInt(), temporada = seasonYear)
                        }
                        // Insertamos el partido con la temporada correcta
                        db.partidoDao().insert(
                            fixture.toPartidoEntity().copy(idTemporada = temporada.id)
                        )
                    } catch (e: android.database.sqlite.SQLiteConstraintException) {
                        Log.w(TAG, "Partido ${fixture.fixture.id} ignorado por FK inexistente")
                    }
                }
                // Registramos que ya hemos consultado este equipo
                db.apiSyncDao().markAsSynced(
                    ApiSyncEntity(
                        tipo = "team",
                        idExterno = teamId,
                        ultimaSync = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    )
                )
                Log.d(TAG, "Partidos del equipo $teamId guardados en Room: ${finalizados.size}")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener partidos equipo $teamId: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener partidos equipo $teamId", e)
            false
        }
    }

    suspend fun fetchAndSaveFixturePlayers(
        apiKey: String,
        fixtureId: Int
    ): Boolean {
        return try {
            val yaSincronizado = db.apiSyncDao().hasSynced("fixture", fixtureId) > 0
            if (yaSincronizado) {
                Log.d(TAG, "Jugadores del partido $fixtureId ya sincronizados, omitiendo llamada")
                return true
            }
            val response = api.getFixturePlayers(BuildConfig.API_FOOTBALL_KEY, fixtureId)
            if (response.isSuccessful) {
                val equipos = response.body()?.response ?: emptyList()
                equipos.forEach { equipoData ->
                    val idEquipo = equipoData.team.id
                    equipoData.players.forEach { playerData ->
                        try {
                            val jugador = playerData.toJugadorEntity(idEquipo)
                            val partidoJugador = playerData.toPartidoJugadorEntity(fixtureId, idEquipo)
                            if (jugador == null || partidoJugador == null) {
                                return@forEach
                            }
                            // Solo insertamos jugadores que hayan jugado minutos
                            // Los convocados que no jugaron tienen minutes == null
                            val minutos = partidoJugador.minutosJugados ?: 0
                            if (minutos == 0) {
                                return@forEach
                            }
                            db.jugadorDao().insert(jugador)
                            db.partidoJugadorDao().insert(partidoJugador)
                        } catch (e: android.database.sqlite.SQLiteConstraintException) {
                            Log.w(TAG, "Jugador ${playerData.player?.id} ignorado por FK: ${e.message}")
                        }
                    }
                }
                db.apiSyncDao().markAsSynced(
                    ApiSyncEntity(
                        tipo = "fixture",
                        idExterno = fixtureId,
                        ultimaSync = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    )
                )
                Log.d(TAG, "Jugadores del partido $fixtureId guardados en Room")
                true
            } else {
                Log.e(TAG, "Error de la API al obtener jugadores partido $fixtureId: ${response.code()}")
                false
            }
        } catch (e: Exception) {
            Log.e(TAG, "Excepción al obtener jugadores partido $fixtureId", e)
            false
        }
    }
}