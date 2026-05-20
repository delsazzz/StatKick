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
import com.example.app_futbol_tfg.data.entity.LocalidadEntity

private const val TAG = "ApiFootballRepository"
// Repositorio encargado de sincronizar datos entre API-Football y Room
// Implementa una arquitectura offline-first donde la API alimenta la base de datos local
// y la interfaz trabaja siempre sobre datos persistidos en Room
class ApiFootballRepository(
    private val api: ApiFootballService,
    val db: AppDatabase
) {
    // Obtiene partidos desde la API y los almacena en Room
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
    // Sincroniza competiciones desde API-Football hacia la base de datos local
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
    // Sincroniza equipos, estadios y localizaciones asociadas
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
                    // Se asegura la existencia previa de país y localidad antes de insertar relaciones dependientes
                    val nombrePais = team.team.country ?: team.venue?.country
                    val paisEntity = nombrePais?.let { pais ->
                        db.paisDao().getByNombre(pais)
                            ?: run {
                                val newPaisId = db.paisDao().insert(
                                    com.example.app_futbol_tfg.data.entity.PaisEntity(
                                        nombre = pais,
                                        bandera = null
                                    )
                                ).toInt()
                                com.example.app_futbol_tfg.data.entity.PaisEntity(
                                    id = newPaisId,
                                    nombre = pais,
                                    bandera = null
                                )
                            }
                    }
                    val localidadEntity = team.venue?.city?.let { nombreLocalidad ->
                        paisEntity?.let { pais ->
                            db.localidadDao().getByNombreAndPais(
                                nombre = nombreLocalidad,
                                idPais = pais.id
                            )
                        }
                    }
                    val idLocalidad = if (
                        localidadEntity == null &&
                        team.venue?.city != null &&
                        paisEntity != null
                    ) {
                        db.localidadDao().insert(
                            LocalidadEntity(
                                nombre = team.venue.city,
                                idPais = paisEntity.id
                            )
                        ).toInt()
                    } else {
                        localidadEntity?.id
                    }
                    // Inserción del estadio asociado al equipo
                    team.toEstadioEntity()?.let { estadio ->
                        db.estadioDao().insert(
                            estadio.copy(
                                idLocalidad = idLocalidad,
                                idPais = paisEntity?.id
                            )
                        )
                    }
                    // Inserción final del equipo con referencias geográficas asociadas
                    db.equipoDao().insert(
                        team.toEquipoEntity().copy(
                            idPais = paisEntity?.id,
                            idLocalidad = idLocalidad
                        )
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
    // Descarga y almacena jugadores asociados a un equipo y temporada
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
                        // Se asocia el jugador con su equipo actual antes de persistirlo
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
    // Sincroniza países desde la API evitando llamadas innecesarias si ya existen datos locales
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
    // Descarga una competición concreta utilizada durante la carga inicial de datos.
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
    // Sincroniza partidos de una competición concreta
    // Se evita repetir llamadas ya realizadas para optimizar la cuota de la API
    suspend fun fetchAndSaveFixturesByLeague(
        apiKey: String,
        leagueId: Int,
        season: Int
    ): Boolean {
        return try {
            // Verificación de sincronización previa para evitar llamadas duplicadas
            val yaSincronizado = db.apiSyncDao().hasSynced("league", leagueId) > 0
            if (yaSincronizado) {
                Log.d(TAG, "Partidos de liga $leagueId ya sincronizados, omitiendo llamada")
                return true
            }
            val response = api.getFixtures(apiKey, leagueId = leagueId, season = season)
            if (response.isSuccessful) {
                val fixtures = response.body()?.response ?: emptyList()
                // Solo se almacenan partidos finalizados
                val finalizados = fixtures.filter { it.fixture.status.short == "FT" }
                finalizados.forEach { fixture ->
                    try {
                        // Se asegura la existencia previa de la temporada antes de insertar el partido
                        val seasonYear = fixture.league.season?.toString() ?: "Desconocida"
                        var temporada = db.temporadaDao().getByTemporada(seasonYear)
                        if (temporada == null) {
                            val newId = db.temporadaDao().insert(
                                TemporadaEntity(temporada = seasonYear)
                            )
                            temporada = TemporadaEntity(id = newId.toInt(), temporada = seasonYear)
                        }
                        // Inserción final del partido con referencias consistentes
                        db.partidoDao().insert(
                            fixture.toPartidoEntity().copy(idTemporada = temporada.id)
                        )
                    } catch (e: android.database.sqlite.SQLiteConstraintException) {
                        Log.w(TAG, "Partido ${fixture.fixture.id} ignorado por FK inexistente")
                    }
                }
                // Registro de sincronización para evitar futuras llamadas redundantes
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
    // de ese equipo ya en Room para ahorrar cuota
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
                        val seasonYear = fixture.league.season?.toString() ?: "Desconocida"
                        var temporada = db.temporadaDao().getByTemporada(seasonYear)
                        if (temporada == null) {
                            val newId = db.temporadaDao().insert(
                                TemporadaEntity(temporada = seasonYear)
                            )
                            temporada = TemporadaEntity(id = newId.toInt(), temporada = seasonYear)
                        }
                        db.partidoDao().insert(
                            fixture.toPartidoEntity().copy(idTemporada = temporada.id)
                        )
                    } catch (e: android.database.sqlite.SQLiteConstraintException) {
                        Log.w(TAG, "Partido ${fixture.fixture.id} ignorado por FK inexistente")
                    }
                }
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
    // Sincroniza jugadores participantes y estadísticas individuales de un partido concreto
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
            // Consulta detallada de alineaciones y estadísticas del encuentro
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
                            // Solo se almacenan jugadores que hayan participado realmente en el partido
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
                // Registro de sincronización completada del partido
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