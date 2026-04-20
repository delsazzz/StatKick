package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API para el endpoint /leagues
data class LeaguesResponse(
    @SerializedName("response") val response: List<LeagueItem>
)

// Cada elemento de la lista de ligas
data class LeagueItem(
    @SerializedName("league") val league: LeagueDetail,
    @SerializedName("country") val country: CountryInfo
)

// Detalle de la liga/competición
data class LeagueDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("logo") val logo: String?
)

// País de la competición
data class CountryInfo(
    @SerializedName("name") val name: String?,
    @SerializedName("flag") val flag: String?
)