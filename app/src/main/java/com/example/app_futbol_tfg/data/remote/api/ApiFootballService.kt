package com.example.app_futbol_tfg.data.remote.api

import com.example.app_futbol_tfg.data.remote.model.CountriesResponse
import com.example.app_futbol_tfg.data.remote.model.FixturePlayersResponse
import com.example.app_futbol_tfg.data.remote.model.FixturesResponse
import com.example.app_futbol_tfg.data.remote.model.LeaguesResponse
import com.example.app_futbol_tfg.data.remote.model.TeamsResponse
import com.example.app_futbol_tfg.data.remote.model.PlayersResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

// Interfaz que define todos los endpoints utilizados para comunicarse con API-Football
// Retrofit genera automáticamente la implementación de cada petición HTTP
interface ApiFootballService {
    // Recupera partidos filtrados por competición y temporada
    @GET("fixtures")
    suspend fun getFixtures(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<FixturesResponse>
    // Recupera el listado de competiciones disponibles
    @GET("leagues")
    suspend fun getLeagues(
        @Header("x-apisports-key") apiKey: String
    ): Response<LeaguesResponse>
    // Recupera equipos asociados a una competición y temporada
    @GET("teams")
    suspend fun getTeams(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<TeamsResponse>
    // Recupera jugadores pertenecientes a un equipo y temporada
    @GET("players")
    suspend fun getPlayers(
        @Header("x-apisports-key") apiKey: String,
        @Query("team") teamId: Int,
        @Query("season") season: Int
    ): Response<PlayersResponse>
    // Recupera el listado de países disponibles en la API
    @GET("countries")
    suspend fun getCountries(
        @Header("x-apisports-key") apiKey: String
    ): Response<CountriesResponse>
    // Recupera una competición concreta filtrando por identificador
    @GET("leagues")
    suspend fun getLeagues(
        @Header("x-apisports-key") apiKey: String,
        @Query("id") leagueId: Int? = null
    ): Response<LeaguesResponse>
    // Recupera partidos filtrando opcionalmente por competición o equipo
    @GET("fixtures")
    suspend fun getFixtures(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int? = null,
        @Query("team") teamId: Int? = null,
        @Query("season") season: Int
    ): Response<FixturesResponse>
    // Recupera alineaciones y estadísticas individuales de un partido concreto
    @GET("fixtures/players")
    suspend fun getFixturePlayers(
        @Header("x-apisports-key") apiKey: String,
        @Query("fixture") fixtureId: Int
    ): Response<FixturePlayersResponse>
}