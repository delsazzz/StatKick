package com.example.app_futbol_tfg.data.remote.model

import com.google.gson.annotations.SerializedName

// Respuesta completa de la API para el endpoint /countries
data class CountriesResponse(
    @SerializedName("response") val response: List<CountryItem>
)

// Cada país devuelto por la API
data class CountryItem(
    @SerializedName("name") val name: String?,
    @SerializedName("code") val code: String?,
    @SerializedName("flag") val flag: String?
)