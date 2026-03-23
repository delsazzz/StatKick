package com.example.app_futbol_tfg.data.database

import com.example.app_futbol_tfg.data.entity.CompeticionEntity
import com.example.app_futbol_tfg.data.entity.EquipoEntity
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import com.example.app_futbol_tfg.data.entity.JugadorEntity
import com.example.app_futbol_tfg.data.entity.LocalidadEntity
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.entity.PaisEntity
import com.example.app_futbol_tfg.data.entity.PartidoEntity
import com.example.app_futbol_tfg.data.entity.PartidoJugadorEntity
import com.example.app_futbol_tfg.data.entity.TemporadaEntity
import com.example.app_futbol_tfg.data.entity.UsuarioEntity
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity

object SeedData {

    suspend fun seed(database: AppDatabase) {
        val paisDao = database.paisDao()
        val localidadDao = database.localidadDao()
        val estadioDao = database.estadioDao()
        val competicionDao = database.competicionDao()
        val temporadaDao = database.temporadaDao()
        val equipoDao = database.equipoDao()
        val jugadorDao = database.jugadorDao()
        val partidoDao = database.partidoDao()
        val partidoJugadorDao = database.partidoJugadorDao()
        val usuarioDao = database.usuarioDao()
        val logroDao = database.logroDao()
        val usuarioPartidoDao = database.usuarioPartidoDao()

        // Evita reseed si ya hay datos
        val paisesExistentes = paisDao.getById(1)
        if (paisesExistentes != null) return

        // PAISES
        val idEspana = paisDao.insert(PaisEntity(nombre = "España", bandera = "espana")).toInt()
        val idInglaterra = paisDao.insert(PaisEntity(nombre = "Inglaterra", bandera = "inglaterra")).toInt()
        val idItalia = paisDao.insert(PaisEntity(nombre = "Italia", bandera = "italia")).toInt()
        val idFrancia = paisDao.insert(PaisEntity(nombre = "Francia", bandera = "francia")).toInt()

        // LOCALIDADES
        val idMadrid = localidadDao.insert(LocalidadEntity(nombre = "Madrid", idPais = idEspana)).toInt()
        val idBarcelona = localidadDao.insert(LocalidadEntity(nombre = "Barcelona", idPais = idEspana)).toInt()
        val idManchester = localidadDao.insert(LocalidadEntity(nombre = "Manchester", idPais = idInglaterra)).toInt()
        val idTurin = localidadDao.insert(LocalidadEntity(nombre = "Turín", idPais = idItalia)).toInt()
        val idParis = localidadDao.insert(LocalidadEntity(nombre = "París", idPais = idFrancia)).toInt()

        // ESTADIOS
        val idBernabeu = estadioDao.insert(EstadioEntity(nombre = "Santiago Bernabéu", idLocalidad = idMadrid,idPais = idEspana,
                                                        capacidad = 81044)).toInt()
        val idCampNou = estadioDao.insert(EstadioEntity(nombre = "Camp Nou",idLocalidad = idBarcelona,idPais = idEspana,
                                                        capacidad = 99354)).toInt()
        val idOldTrafford = estadioDao.insert(EstadioEntity(nombre = "Old Trafford",idLocalidad = idManchester,idPais = idInglaterra,
                                                        capacidad = 74140)).toInt()
        val idAllianz = estadioDao.insert(EstadioEntity(nombre = "Allianz Stadium",idLocalidad = idTurin,idPais = idItalia,
                                                        capacidad = 41507)).toInt()
        val idParcDesPrinces = estadioDao.insert(EstadioEntity(nombre = "Parc des Princes",idLocalidad = idParis,idPais = idFrancia,
                                                        capacidad = 47929)).toInt()

        // COMPETICIONES
        val idLaLiga = competicionDao.insert(CompeticionEntity(nombre = "LaLiga", idPais = idEspana)).toInt()
        val idPremier = competicionDao.insert(CompeticionEntity(nombre = "Premier League", idPais = idInglaterra)).toInt()
        val idChampions = competicionDao.insert(CompeticionEntity(nombre = "Champions League", idPais = null)).toInt()

        // TEMPORADAS
        val idTemp2324 = temporadaDao.insert(TemporadaEntity(temporada = "2023/2024")).toInt()
        val idTemp2425 = temporadaDao.insert(TemporadaEntity(temporada = "2024/2025")).toInt()

        // EQUIPOS
        val idRealMadrid = equipoDao.insert(EquipoEntity(nombre = "Real Madrid",anioFundacion = 1902,idEstadio = idBernabeu,
                                                        idLocalidad = idMadrid, idPais = idEspana, escudo = "escudo_real_madrid")).toInt()
        val idBarcelonaEquipo = equipoDao.insert(EquipoEntity(nombre = "FC Barcelona",anioFundacion = 1899,idEstadio = idCampNou,
                                                        idLocalidad = idBarcelona,idPais = idEspana, escudo = "escudo_barcelona")).toInt()
        val idManchesterUnited = equipoDao.insert(EquipoEntity(nombre = "Manchester United",anioFundacion = 1878,idEstadio = idOldTrafford,
                                                        idLocalidad = idManchester,idPais = idInglaterra,escudo = "escudo_manchester_united")).toInt()
        val idJuventus = equipoDao.insert(EquipoEntity(nombre = "Juventus",anioFundacion = 1897,idEstadio = idAllianz,idLocalidad = idTurin,
                                                        idPais = idItalia, escudo = "escudo_juventus")).toInt()
        val idPSG = equipoDao.insert(EquipoEntity(nombre = "PSG",anioFundacion = 1970,idEstadio = idParcDesPrinces,idLocalidad = idParis,
                                                idPais = idFrancia,escudo = "escudo_psg")).toInt()
        val idEspanaSeleccion = equipoDao.insert(EquipoEntity(nombre = "España",anioFundacion = null,idEstadio = null,idLocalidad = null,
                                                idPais = idEspana, escudo = "escudo_seleccion_espana")).toInt()
        val idFranciaSeleccion = equipoDao.insert(EquipoEntity(nombre = "Francia",anioFundacion = null,idEstadio = null,idLocalidad = null,
                                                idPais = idFrancia,escudo = "escudo_seleccion_francia")).toInt()

        // JUGADORES
        val idBellingham = jugadorDao.insert(JugadorEntity(nombre = "Jude",apellido1 = "Bellingham",apellido2 = null,fechaNacimiento = "2003-06-29",
                                            idLocalidad = idManchester,idPais = idInglaterra,idEquipoActual = idRealMadrid,posicion = "Centrocampista",
                                            activo = true)).toInt()

        val idVinicius = jugadorDao.insert(JugadorEntity(nombre = "Vinicius",apellido1 = "Junior",apellido2 = null,fechaNacimiento = "2000-07-12",
                                                        idLocalidad = null,idPais = null,idEquipoActual = idRealMadrid,posicion = "Delantero",
                                                        activo = true)).toInt()

        val idLewandowski = jugadorDao.insert(JugadorEntity(nombre = "Robert",apellido1 = "Lewandowski",apellido2 = null,fechaNacimiento = "1988-08-21",
                                                        idLocalidad = null,idPais = null,idEquipoActual = idBarcelonaEquipo,posicion = "Delantero",
                                                        activo = true)).toInt()

        val idPedri = jugadorDao.insert(
            JugadorEntity(
                nombre = "Pedri",
                apellido1 = "González",
                apellido2 = null,
                fechaNacimiento = "2002-11-25",
                idLocalidad = null,
                idPais = idEspana,
                idEquipoActual = idBarcelonaEquipo,
                posicion = "Centrocampista",
                activo = true
            )
        ).toInt()

        val idBruno = jugadorDao.insert(
            JugadorEntity(
                nombre = "Bruno",
                apellido1 = "Fernandes",
                apellido2 = null,
                fechaNacimiento = "1994-09-08",
                idLocalidad = null,
                idPais = null,
                idEquipoActual = idManchesterUnited,
                posicion = "Centrocampista",
                activo = true
            )
        ).toInt()

        val idRashford = jugadorDao.insert(
            JugadorEntity(
                nombre = "Marcus",
                apellido1 = "Rashford",
                apellido2 = null,
                fechaNacimiento = "1997-10-31",
                idLocalidad = idManchester,
                idPais = idInglaterra,
                idEquipoActual = idManchesterUnited,
                posicion = "Delantero",
                activo = true
            )
        ).toInt()

        val idVlahovic = jugadorDao.insert(
            JugadorEntity(
                nombre = "Dusan",
                apellido1 = "Vlahovic",
                apellido2 = null,
                fechaNacimiento = "2000-01-28",
                idLocalidad = null,
                idPais = null,
                idEquipoActual = idJuventus,
                posicion = "Delantero",
                activo = true
            )
        ).toInt()

        val idMbappe = jugadorDao.insert(
            JugadorEntity(
                nombre = "Kylian",
                apellido1 = "Mbappé",
                apellido2 = null,
                fechaNacimiento = "1998-12-20",
                idLocalidad = idParis,
                idPais = idFrancia,
                idEquipoActual = idPSG,
                posicion = "Delantero",
                activo = true
            )
        ).toInt()

        val idMorata = jugadorDao.insert(
            JugadorEntity(
                nombre = "Álvaro",
                apellido1 = "Morata",
                apellido2 = null,
                fechaNacimiento = "1992-10-23",
                idLocalidad = idMadrid,
                idPais = idEspana,
                idEquipoActual = idEspanaSeleccion,
                posicion = "Delantero",
                activo = true
            )
        ).toInt()

        val idJugadorLibre = jugadorDao.insert(
            JugadorEntity(
                nombre = "Sergio",
                apellido1 = "Libre",
                apellido2 = null,
                fechaNacimiento = "1995-05-10",
                idLocalidad = null,
                idPais = idEspana,
                idEquipoActual = null,
                posicion = "Defensa",
                activo = true
            )
        ).toInt()

        // =========================
        // PARTIDOS
        // =========================
        val idPartido1 = partidoDao.insert(
            PartidoEntity(
                idEquipoLocal = idRealMadrid,
                idEquipoVisitante = idBarcelonaEquipo,
                golesLocal = 2,
                golesVisitante = 1,
                fecha = "2024-04-21",
                idTemporada = idTemp2324,
                idCompeticion = idLaLiga,
                idEstadio = idBernabeu,
                jornada = "Jornada 32"
            )
        ).toInt()

        val idPartido2 = partidoDao.insert(
            PartidoEntity(
                idEquipoLocal = idManchesterUnited,
                idEquipoVisitante = idJuventus,
                golesLocal = 1,
                golesVisitante = 1,
                fecha = "2024-03-12",
                idTemporada = idTemp2324,
                idCompeticion = idChampions,
                idEstadio = idOldTrafford,
                jornada = "Octavos"
            )
        ).toInt()

        val idPartido3 = partidoDao.insert(
            PartidoEntity(
                idEquipoLocal = idPSG,
                idEquipoVisitante = idBarcelonaEquipo,
                golesLocal = 2,
                golesVisitante = 3,
                fecha = "2024-04-10",
                idTemporada = idTemp2324,
                idCompeticion = idChampions,
                idEstadio = idParcDesPrinces,
                jornada = "Cuartos"
            )
        ).toInt()

        val idPartido4 = partidoDao.insert(
            PartidoEntity(
                idEquipoLocal = idEspanaSeleccion,
                idEquipoVisitante = idFranciaSeleccion,
                golesLocal = 2,
                golesVisitante = 0,
                fecha = "2024-06-15",
                idTemporada = idTemp2324,
                idCompeticion = null,
                idEstadio = idBernabeu,
                jornada = "Amistoso"
            )
        ).toInt()

        // =========================
        // PARTICIPACION JUGADORES EN PARTIDOS
        // =========================
        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido1,
                idJugador = idBellingham,
                idEquipo = idRealMadrid,
                titular = true,
                minutosJugados = 90,
                goles = 1,
                asistencias = 0,
                amarillas = 1,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido1,
                idJugador = idVinicius,
                idEquipo = idRealMadrid,
                titular = true,
                minutosJugados = 85,
                goles = 1,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido1,
                idJugador = idLewandowski,
                idEquipo = idBarcelonaEquipo,
                titular = true,
                minutosJugados = 90,
                goles = 1,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido1,
                idJugador = idPedri,
                idEquipo = idBarcelonaEquipo,
                titular = true,
                minutosJugados = 88,
                goles = 0,
                asistencias = 1,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido2,
                idJugador = idBruno,
                idEquipo = idManchesterUnited,
                titular = true,
                minutosJugados = 90,
                goles = 1,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido2,
                idJugador = idRashford,
                idEquipo = idManchesterUnited,
                titular = true,
                minutosJugados = 82,
                goles = 0,
                asistencias = 1,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido2,
                idJugador = idVlahovic,
                idEquipo = idJuventus,
                titular = true,
                minutosJugados = 90,
                goles = 1,
                asistencias = 0,
                amarillas = 1,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido3,
                idJugador = idMbappe,
                idEquipo = idPSG,
                titular = true,
                minutosJugados = 90,
                goles = 1,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido3,
                idJugador = idLewandowski,
                idEquipo = idBarcelonaEquipo,
                titular = true,
                minutosJugados = 90,
                goles = 2,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        partidoJugadorDao.insert(
            PartidoJugadorEntity(
                idPartido = idPartido4,
                idJugador = idMorata,
                idEquipo = idEspanaSeleccion,
                titular = true,
                minutosJugados = 78,
                goles = 1,
                asistencias = 0,
                amarillas = 0,
                rojas = 0
            )
        )

        // =========================
        // USUARIOS
        // =========================
        val idAdmin = usuarioDao.insert(
            UsuarioEntity(
                nombreUsuario = "admin",
                email = "admin@futbol.com",
                passwordHash = "hash_admin_123",
                fechaRegistro = "2026-03-18",
                rol = "admin"
            )
        ).toInt()

        val idDemo = usuarioDao.insert(
            UsuarioEntity(
                nombreUsuario = "usuario_demo",
                email = "demo@futbol.com",
                passwordHash = "hash_demo_123",
                fechaRegistro = "2026-03-18",
                rol = "usuario"
            )
        ).toInt()

        // =========================
        // LOGROS
        // =========================
        val idLogro1 = logroDao.insertLogro(
            LogroEntity(
                nombre = "Primer partido guardado",
                descripcion = "Has guardado tu primer partido"
            )
        ).toInt()

        val idLogro2 = logroDao.insertLogro(
            LogroEntity(
                nombre = "Fan del fútbol",
                descripcion = "Has consultado varios partidos"
            )
        ).toInt()

        val idLogro3 = logroDao.insertLogro(
            LogroEntity(
                nombre = "Coleccionista",
                descripcion = "Has guardado más de un partido"
            )
        ).toInt()

        // =========================
        // USUARIO_LOGRO
        // =========================
        logroDao.asignarLogro(
            UsuarioLogroEntity(
                idUsuario = idDemo,
                idLogro = idLogro1,
                fechaObtenido = "2026-03-18"
            )
        )

        logroDao.asignarLogro(
            UsuarioLogroEntity(
                idUsuario = idDemo,
                idLogro = idLogro2,
                fechaObtenido = "2026-03-18"
            )
        )

        logroDao.asignarLogro(
            UsuarioLogroEntity(
                idUsuario = idAdmin,
                idLogro = idLogro3,
                fechaObtenido = "2026-03-18"
            )
        )

        // =========================
        // USUARIO_PARTIDO
        // =========================
        usuarioPartidoDao.insert(
            UsuarioPartidoEntity(
                idUsuario = idDemo,
                idPartido = idPartido1,
                fechaRegistro = "2026-03-18"
            )
        )

        usuarioPartidoDao.insert(
            UsuarioPartidoEntity(
                idUsuario = idDemo,
                idPartido = idPartido3,
                fechaRegistro = "2026-03-18"
            )
        )

        usuarioPartidoDao.insert(
            UsuarioPartidoEntity(
                idUsuario = idAdmin,
                idPartido = idPartido2,
                fechaRegistro = "2026-03-18"
            )
        )
    }
}