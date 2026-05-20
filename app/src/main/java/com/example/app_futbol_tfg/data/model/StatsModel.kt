package com.example.app_futbol_tfg.data.model

// Para ranking de equipos vistos
data class TeamSeenStat(
    val id: Int,
    val nombre: String,
    val escudo: String?,
    val vecesVisto: Int
)
// Para ranking de jugadores, goleadores y asistentes vistos
data class PlayerSeenStat(
    val id: Int,
    val nombre: String,
    val apellido1: String?,
    val escudo: String?,
    val bandera: String?,
    val total: Int
)
// Para rangking de estadios vistos
data class StadiumSeenStat(
    val id: Int,
    val nombre: String,
    val total: Int
)
// Para ranking de tarjetas
data class CardStats(
    val amarillas: Int,
    val rojas: Int
)