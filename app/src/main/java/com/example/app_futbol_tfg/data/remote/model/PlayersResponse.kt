package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API para el endpoint /players
data class PlayersResponse(
    @SerializedName("response") val response: List<PlayerItem>
)

// Cada elemento de la lista de jugadores
data class PlayerItem(
    @SerializedName("player") val player: PlayerDetail
)

// Detalle del jugador
data class PlayerDetail(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("firstname") val firstname: String?,
    @SerializedName("lastname") val lastname: String?,
    @SerializedName("nationality") val nationality: String?
)