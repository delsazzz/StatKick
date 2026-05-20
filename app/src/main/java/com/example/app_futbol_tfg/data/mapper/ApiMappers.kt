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

// Funciones mapper encargadas de transformar respuestas de API-Football
// en entidades persistibles compatibles con Room
// Convierte una competición recibida desde la API en una entidad local.
fun LeagueItem.toCompeticionEntity(): CompeticionEntity {
    return CompeticionEntity(
        id = this.league.id,
        nombre = this.league.name ?: "Sin nombre",
        idPais = null,
        logo = this.league.logo // URL del logo
    )
}
// Convierte un equipo remoto en una entidad persistible de Room
fun TeamItem.toEquipoEntity(): EquipoEntity {
    return EquipoEntity(
        id = this.team.id,
        nombre = this.team.name ?: "Sin nombre",
        anioFundacion = null,
        idEstadio = this.venue?.id,
        idLocalidad = null,
        idPais = null,
        // El escudo se almacena como URL remota proporcionada por la API
        escudo = this.team.logo ?: ""
    )
}
// Convierte la información de estadio asociada a un equipo en una entidad local
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
// Convierte un partido recibido desde la API en una entidad local persistible
fun FixtureItem.toPartidoEntity(): PartidoEntity {
    // Se normaliza la fecha para mantener consistencia con el formato almacenado en Room
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
// Convierte información de jugador remota en una entidad local
fun PlayerItem.toJugadorEntity(): JugadorEntity {
    return JugadorEntity(
        id = this.player.id,
        nombre = this.player.firstname ?: this.player.name ?: "Sin nombre",
        apellido1 = this.player.lastname,
        apellido2 = null,
        fechaNacimiento = null,
        idLocalidad = null,
        idPais = null,
        // El equipo actual se asigna posteriormente durante la sincronización
        idEquipoActual = null,
        posicion = null,
        activo = true
    )
}
// Convierte países recibidos desde la API en entidades persistibles
fun CountryItem.toPaisEntity(): PaisEntity {
    return PaisEntity(
        id = 0, // AutoGenerate, Room asigna el id
        nombre = this.name ?: "Sin nombre",
        bandera = this.flag // URL tipo https://media.api-sports.io/flags/es.svg
    )
}
// Convierte jugadores participantes de un partido en entidades locales
fun FixturePlayerData.toJugadorEntity(idEquipo: Int): JugadorEntity? {
    val playerInfo = this.player ?: return null
    val playerId = playerInfo.id ?: return null
    // Separación básica del nombre completo para adaptar el modelo de datos local
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
// Convierte estadísticas individuales de un partido en relaciones Partido_Jugador
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
        // Se considera titular al jugador que no figura como suplente
        titular = games?.substitute == false,
        minutosJugados = games?.minutes,
        goles = goals?.total ?: 0,
        asistencias = goals?.assists ?: 0,
        amarillas = cards?.yellow ?: 0,
        rojas = cards?.red ?: 0
    )
}