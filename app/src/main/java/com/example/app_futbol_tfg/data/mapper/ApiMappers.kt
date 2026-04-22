package com.example.app_futbol_tfg.data.mapper

import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import com.example.app_futbol_tfg.data.entity.PaisEntity
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.remote.model.CountryItem
import com.example.app_futbol_tfg.data.remote.model.FixtureItem
import com.example.app_futbol_tfg.data.remote.model.LeagueItem
import com.example.app_futbol_tfg.data.remote.model.PlayerItem
import com.example.app_futbol_tfg.data.remote.model.TeamItem
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import com.example.app_futbol_tfg.data.remote.model.FixturePlayerData
import com.example.app_futbol_tfg.data.remote.model.FixturePlayersTeam

// Convierte una respuesta de liga de la API a una entidad de Room
fun LeagueItem.toCompeticionEntity(): CompeticionEntity {
    return CompeticionEntity(
        id = this.league.id,
        nombre = this.league.name ?: "Sin nombre",
        idPais = null
    )
}

// Convierte una respuesta de equipo de la API a una entidad de Room
fun TeamItem.toEquipoEntity(): EquipoEntity {
    return EquipoEntity(
        id = this.team.id,
        nombre = this.team.name ?: "Sin nombre",
        anioFundacion = null,
        idEstadio = this.venue?.id,
        idLocalidad = null,
        idPais = null,
        // El escudo viene como URL desde la API, no como drawable local
        escudo = this.team.logo ?: ""
    )
}

// Convierte la info del estadio de un equipo a una entidad de Room.
// Devuelve null si el estadio no tiene id o no existe en la respuesta
fun TeamItem.toEstadioEntity(): EstadioEntity? {
    val venue = this.venue ?: return null
    val venueId = venue.id ?: return null
    return EstadioEntity(
        id = venueId,
        nombre = venue.name ?: "Sin nombre",
        idLocalidad = null,
        idPais = null,
        capacidad = null
    )
}

// Convierte un partido de la API a una entidad de Room
fun FixtureItem.toPartidoEntity(): PartidoEntity {
    // La fecha viene en formato ISO 8601 (2024-01-15T20:00:00+00:00)
    // Nos quedamos solo con la parte YYYY-MM-DD para ser consistentes con la BBDD local
    val fecha = this.fixture.date?.take(10) ?: "0000-00-00"
    return PartidoEntity(
        id = this.fixture.id,
        idEquipoLocal = this.teams.home.id,
        idEquipoVisitante = this.teams.away.id,
        golesLocal = this.goals.home ?: 0,
        golesVisitante = this.goals.away ?: 0,
        fecha = fecha,
        idCompeticion = this.league.id,
        idTemporada = null,
        idEstadio = this.fixture.venue?.id,
        jornada = null
    )
}

// Convierte un jugador de la API a una entidad de Room
fun PlayerItem.toJugadorEntity(): JugadorEntity {
    return JugadorEntity(
        id = this.player.id,
        nombre = this.player.firstname ?: this.player.name ?: "Sin nombre",
        apellido1 = this.player.lastname,
        apellido2 = null,
        fechaNacimiento = null,
        idLocalidad = null,
        idPais = null,
        // El equipo actual se asignará desde fuera cuando se conozca
        idEquipoActual = null,
        posicion = null,
        activo = true
    )
}

// Convierte un país de la API a una entidad de Room.
// La bandera viene como URL desde la API, la guardamos directamente.
fun CountryItem.toPaisEntity(): PaisEntity {
    return PaisEntity(
        id = 0, // autoGenerate, Room asigna el id
        nombre = this.name ?: "Sin nombre",
        bandera = this.flag // URL tipo https://media.api-sports.io/flags/es.svg
    )
}
fun FixturePlayerData.toJugadorEntity(idEquipo: Int): JugadorEntity? {
    val playerInfo = this.player ?: return null
    val playerId = playerInfo.id ?: return null
    val partes = playerInfo.name?.split(" ") ?: emptyList()
    return JugadorEntity(
        id = playerId,
        nombre = partes.firstOrNull() ?: "Sin nombre",
        apellido1 = partes.drop(1).joinToString(" ").ifBlank { null },
        apellido2 = null,
        fechaNacimiento = null,
        idLocalidad = null,
        idPais = null,
        idEquipoActual = idEquipo,
        posicion = null,
        activo = true
    )
}

fun FixturePlayerData.toPartidoJugadorEntity(idPartido: Int, idEquipo: Int): PartidoJugadorEntity? {
    val playerInfo = this.player ?: return null
    val playerId = playerInfo.id ?: return null
    val stats = this.statistics.firstOrNull()
    val games = stats?.games
    val goals = stats?.goals
    val cards = stats?.cards
    return PartidoJugadorEntity(
        id = 0,
        idPartido = idPartido,
        idJugador = playerId,
        idEquipo = idEquipo,
        titular = games?.substitute == false,
        minutosJugados = games?.minutes,
        goles = goals?.total ?: 0,
        asistencias = goals?.assists ?: 0,
        amarillas = cards?.yellow ?: 0,
        rojas = cards?.red ?: 0
    )
}