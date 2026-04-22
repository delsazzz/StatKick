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

// Interfaz que define los endpoints de API-Football que vamos a usar.
// Retrofit genera automáticamente la implementación de cada función.
interface ApiFootballService {

    // Obtiene partidos filtrados por liga y temporada
    @GET("fixtures")
    suspend fun getFixtures(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<FixturesResponse>

    // Obtiene las ligas/competiciones disponibles
    @GET("leagues")
    suspend fun getLeagues(
        @Header("x-apisports-key") apiKey: String
    ): Response<LeaguesResponse>

    // Obtiene equipos de una liga y temporada concreta
    @GET("teams")
    suspend fun getTeams(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int,
        @Query("season") season: Int
    ): Response<TeamsResponse>

    // Obtiene jugadores de un equipo en una temporada concreta
    @GET("players")
    suspend fun getPlayers(
        @Header("x-apisports-key") apiKey: String,
        @Query("team") teamId: Int,
        @Query("season") season: Int
    ): Response<PlayersResponse>

    // Obtiene todos los países disponibles en la API
    @GET("countries")
    suspend fun getCountries(
        @Header("x-apisports-key") apiKey: String
    ): Response<CountriesResponse>

    // Obtiene las ligas/competiciones disponibles, opcionalmente filtradas por id
    @GET("leagues")
    suspend fun getLeagues(
        @Header("x-apisports-key") apiKey: String,
        @Query("id") leagueId: Int? = null
    ): Response<LeaguesResponse>
    // Obtiene partidos filtrados por liga/equipo y temporada
    @GET("fixtures")
    suspend fun getFixtures(
        @Header("x-apisports-key") apiKey: String,
        @Query("league") leagueId: Int? = null,
        @Query("team") teamId: Int? = null,
        @Query("season") season: Int
    ): Response<FixturesResponse>
    // Obtiene las estadísticas de los jugadores de un partido concreto
    @GET("fixtures/players")
    suspend fun getFixturePlayers(
        @Header("x-apisports-key") apiKey: String,
        @Query("fixture") fixtureId: Int
    ): Response<FixturePlayersResponse>
}