package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API para el endpoint /fixtures
data class FixturesResponse(
    @SerializedName("response") val response: List<FixtureItem>
)
// Cada elemento de la lista de partidos
data class FixtureItem(
    @SerializedName("fixture") val fixture: FixtureInfo,
    @SerializedName("league") val league: LeagueInfo,
    @SerializedName("teams") val teams: TeamsInfo,
    @SerializedName("goals") val goals: GoalsInfo,
    @SerializedName("players") val players: List<FixturePlayers>? = null
)
// Información básica del partido
data class FixtureInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String?,
    @SerializedName("venue") val venue: VenueInfo?,
    @SerializedName("status") val status: FixtureStatus
)
// Estado del partido
data class FixtureStatus(
    @SerializedName("short") val short: String?,
    @SerializedName("long") val long: String?
)
// Información del estadio
data class VenueInfo(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: String?
)
// Información de la liga/competición del partido
data class LeagueInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("season") val season: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("flag") val flag: String?
)
// Equipos local y visitante
data class TeamsInfo(
    @SerializedName("home") val home: TeamShortInfo,
    @SerializedName("away") val away: TeamShortInfo
)
// Información básica de un equipo dentro del partido
data class TeamShortInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?
)
// Resultado del partido
data class GoalsInfo(
    @SerializedName("home") val home: Int?,
    @SerializedName("away") val away: Int?
)
// Jugadores del partido agrupados por equipo
data class FixturePlayers(
    @SerializedName("team") val team: TeamShortInfo,
    @SerializedName("players") val players: List<FixturePlayerInfo>
)
// Información de cada jugador en el partido
data class FixturePlayerInfo(
    @SerializedName("player") val player: PlayerShortInfo?,
    @SerializedName("statistics") val statistics: List<PlayerStatistics>
)
// Identificador y nombre del jugador
data class PlayerShortInfo(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?
)
// Estadísticas del jugador en el partido
data class PlayerStatistics(
    @SerializedName("games") val games: GamesStats?,
    @SerializedName("goals") val goals: GoalStats?,
    @SerializedName("cards") val cards: CardStats?
)
// Minutos jugados y si fue titular
data class GamesStats(
    @SerializedName("minutes") val minutes: Int?,
    @SerializedName("substitute") val substitute: Boolean?
)
// Goles y asistencias
data class GoalStats(
    @SerializedName("total") val total: Int?,
    @SerializedName("assists") val assists: Int?
)
// Tarjetas amarillas y rojas
data class CardStats(
    @SerializedName("yellow") val yellow: Int?,
    @SerializedName("red") val red: Int?
)