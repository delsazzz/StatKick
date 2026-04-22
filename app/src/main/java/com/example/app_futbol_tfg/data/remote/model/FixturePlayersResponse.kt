package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa del endpoint /fixtures/players
data class FixturePlayersResponse(
    @SerializedName("response") val response: List<FixturePlayersTeam>
)

// Jugadores agrupados por equipo
data class FixturePlayersTeam(
    @SerializedName("team") val team: TeamShortInfo,
    @SerializedName("players") val players: List<FixturePlayerData>
)

// Datos de cada jugador en el partido
// Datos de cada jugador en el partido
data class FixturePlayerData(
    @SerializedName("player") val player: FixturePlayerBasicInfo?,
    @SerializedName("statistics") val statistics: List<FixturePlayerStats>
)

// Info básica del jugador en el endpoint /fixtures/players
// Es distinta a PlayerShortInfo porque este endpoint devuelve más campos
data class FixturePlayerBasicInfo(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("photo") val photo: String?
)

// Estadísticas del jugador en el partido
data class FixturePlayerStats(
    @SerializedName("games") val games: FixturePlayerGames?,
    @SerializedName("goals") val goals: FixturePlayerGoals?,
    @SerializedName("cards") val cards: FixturePlayerCards?
)

// Minutos jugados y si fue titular
data class FixturePlayerGames(
    @SerializedName("minutes") val minutes: Int?,
    @SerializedName("substitute") val substitute: Boolean?
)

// Goles y asistencias
data class FixturePlayerGoals(
    @SerializedName("total") val total: Int?,
    @SerializedName("assists") val assists: Int?
)

// Tarjetas
data class FixturePlayerCards(
    @SerializedName("yellow") val yellow: Int?,
    @SerializedName("red") val red: Int?
)