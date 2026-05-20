package com.example.app_futbol_tfg.data.remote.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Cliente Retrofit centralizado utilizado para gestionar todas las conexiones HTTP de la aplicación
object RetrofitClient {
    // URL base utilizada por Retrofit para construir las peticiones a API-Football
    private const val BASE_URL = "https://v3.football.api-sports.io/"
    // Configuración de Gson utilizada para convertir automáticamente respuestas JSON en objetos Kotlin
    private val gson = GsonBuilder()
        .serializeNulls()
        .create()
    // Instancia Singleton de Retrofit compartida por toda la aplicación
    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}