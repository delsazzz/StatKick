package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API para el endpoint /teams
data class TeamsResponse(
    @SerializedName("response") val response: List<TeamItem>
)

// Cada elemento de la lista de equipos
data class TeamItem(
    @SerializedName("team") val team: TeamDetail,
    @SerializedName("venue") val venue: VenueDetail?
)

// Detalle del equipo
data class TeamDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?,
    @SerializedName("country") val country: String?
)

// Detalle del estadio del equipo
data class VenueDetail(
    @SerializedName("id") val id: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("country") val country: String?
)