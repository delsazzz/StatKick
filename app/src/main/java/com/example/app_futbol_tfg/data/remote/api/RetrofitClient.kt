package com.example.app_futbol_tfg.data.remote.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Retrofit configurado como Singleton.
// Toda la app usa esta única instancia para hacer llamadas a la API.
object RetrofitClient {

    // URL base de API-Football. Todas las llamadas parten de aquí.
    private const val BASE_URL = "https://v3.football.api-sports.io/"

    // Gson configurado para ser permisivo con valores nulos y tipos inesperados
    // que puedan venir de la API sin romper el parseo
    private val gson = GsonBuilder()
        .serializeNulls()
        .create()

    // Instancia única de Retrofit
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}