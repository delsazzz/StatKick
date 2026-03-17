package com.example.app_futbol_tfg.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import com.example.app_futbol_tfg.data.repository.JugadorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class JugadorViewModel(
    private val repository: JugadorRepository
) : ViewModel() {

    val jugadores: Flow<List<JugadorEntity>> = repository.getAll()

    val jugadoresActivos: Flow<List<JugadorEntity>> = repository.getAllActivos()

    val jugadoresRetirados: Flow<List<JugadorEntity>> = repository.getAllRetirados()

    fun getByEquipo(idEquipo: Int): Flow<List<JugadorEntity>> =
        repository.getByEquipo(idEquipo)

    fun getSinEquipo(): Flow<List<JugadorEntity>> =
        repository.getSinEquipo()

    suspend fun getById(id: Int): JugadorEntity? =
        repository.getById(id)

    fun insertJugador(
        nombre: String,
        apellido1: String?,
        apellido2: String?,
        fechaNacimiento: String?,
        idLocalidad: Int?,
        idPais: Int?,
        idEquipoActual: Int?,
        posicion: String?,
        activo: Boolean = true
    ) {
        viewModelScope.launch {
            repository.insertJugador(
                JugadorEntity(
                    nombre = nombre,
                    apellido1 = apellido1,
                    apellido2 = apellido2,
                    fechaNacimiento = fechaNacimiento,
                    idLocalidad = idLocalidad,
                    idPais = idPais,
                    idEquipoActual = idEquipoActual,
                    posicion = posicion,
                    activo = activo
                )
            )
        }
    }

    fun updateJugador(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.updateJugador(jugador)
        }
    }

    fun deleteJugador(jugador: JugadorEntity) {
        viewModelScope.launch {
            repository.deleteJugador(jugador)
        }
    }
}