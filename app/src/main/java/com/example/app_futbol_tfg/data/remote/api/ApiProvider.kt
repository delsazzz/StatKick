package com.example.app_futbol_tfg.data.remote.api

// Proveedor singleton del servicio de API-Football.
// Se encarga de crear y reutilizar la instancia de ApiFootballService
// a través de Retrofit, evitando crear múltiples instancias innecesarias.
object ApiProvider {

    val apiFootballService: ApiFootballService by lazy {
        RetrofitClient.instance.create(ApiFootballService::class.java)
    }
}