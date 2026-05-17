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

// Aquí insertamos un conjunto inicial de datos de prueba para la BBDD
// Esta carga solo se realiza si la base aún no ha sido inicializada y
// se evita la duplicación de registros en ejecuciones posteriores
object SeedData {
    suspend fun seed(database: AppDatabase) {
        val paisDao = database.paisDao()
        // Si ya existe un registro base, se asume que la BBDD ya se inició
        if (paisDao.getById(1) != null) return

        val ids = SeedIds()

        seedPaises(database, ids)
        seedLocalidades(database, ids)
        seedEstadios(database, ids)
        seedCompeticionesYTemporadas(database, ids)
        seedEquipos(database, ids)
        seedJugadores(database, ids)
        seedPartidos(database, ids)
        seedPartidoJugadores1(database, ids)
        seedPartidoJugadores2(database, ids)
        seedPartidoJugadores3(database, ids)
        seedUsuariosYLogros(database, ids)
    }

    // Esta es una estructura auxiliar que almacena los identificadores generados
    // durante la inserción de datos relacionados entre sí. Se puede reutilizar los ids
    // al crear registros con claves foráneas
    private class SeedIds {
        // Paises
        var idEspana = 0; var idInglaterra = 0; var idItalia = 0; var idFrancia = 0
        var idAlemania = 0; var idPortugal = 0; var idArgentina = 0; var idPaisesBajos = 0
        var idChampionsLeague = 0; var idBelgica = 0; var idEscocia = 0; var idTurquia = 0
        var idAustria = 0; var idRepCheca = 0; var idDinamarca = 0; var idSerbia = 0
        var idUcrania = 0
        // Localidades España
        var idMadrid = 0; var idBarcelona = 0; var idSeVilla = 0; var idValencia = 0
        var idBilbao = 0; var idSanSebastian = 0; var idGetafe = 0; var idPamplona = 0
        var idVitoriaGasteiz = 0; var idVillarreal = 0; var idGirona = 0; var idCadiz = 0
        var idPalmasMallorca = 0; var idPalmaMallorca = 0; var idGranada = 0; var idAlmeria = 0
        var idVigo = 0
        // Localidades Europa
        var idManchester = 0; var idManchester2 = 0; var idNewcastle = 0; var idLondres = 0
        var idLiverpool = 0; var idTurin = 0; var idMilan = 0; var idNapoles = 0
        var idRoma = 0; var idParis = 0; var idMunich = 0; var idDortmund = 0
        var idLisboa = 0; var idOporto = 0; var idBraga = 0; var idAmsterdam = 0
        var idRotterdam = 0; var idEindhoven = 0; var idBuenosAires = 0; var idBrujas = 0
        var idBruselas = 0; var idGlasgow = 0; var idEstambul = 0; var idSalzburg = 0
        var idPraga = 0; var idCopenhague = 0; var idBelgrado = 0; var idKharkiv = 0
        // Estadios originales
        var id_1456 = 0; var id_19939 = 0; var idOldTrafford = 0; var id_909 = 0
        var id_671 = 0; var idAllianzArena = 0; var idSignalIduna = 0; var idDaLuz = 0
        var id_1494 = 0; var id_1497 = 0; var id_550 = 0; var id_494 = 0
        var idDoDragao = 0; var idMonumental = 0; var idJohanCruyffArena = 0
        // Estadios La Liga 2023-24
        var id_19217 = 0; var id_1460 = 0; var id_1491 = 0; var id_1489 = 0
        var idGranCanaria = 0; var idColiseum = 0; var id_1486 = 0; var id_1470 = 0
        var id_1498 = 0; var idVallecas = 0; var idNuevoMirandilla = 0; var idVisitMallorca = 0
        var idLosCarmenes = 0; var idPowerHorse = 0; var idBalaidos = 0; var idMontilivi = 0
        // Estadios Champions 2023-24
        var id_555 = 0; var id_907 = 0; var idMaradona = 0; var idOlimpicoRoma = 0
        var id_1293 = 0; var idEstadioMunicipalBraga = 0; var idDeKuip = 0; var id_1143 = 0
        var id_176 = 0; var idLottoPark = 0; var id_1386 = 0; var id_22761 = 0
        var idSukruSaracoglu = 0; var id_148 = 0; var idGeneraliArena = 0; var idParkenStadion = 0
        var idRajkoMitic = 0; var idShakhtar = 0; var idWembley = 0; var idNewcastleStadium = 0
        // Competiciones
        var idLaLiga = 0; var idPremier = 0; var idChampions = 0; var idBundesliga = 0
        var idPrimeiraLiga = 0; var idEredivisie = 0; var idCopaAmistosa = 0
        // Temporadas
        var idTemp2526 = 0; var idTemp2425 = 0; var idTemp2324 = 0; var idTemp2223 = 0
        var idTemp2122 = 0
        // Equipos
        var idRealMadrid = 0; var idFCBarcelona = 0; var idManchesterUnited = 0; var idJuventus = 0
        var idPSG = 0; var idEspanaSeleccion = 0; var idFranciaSeleccion = 0; var idBayernMunich = 0
        var idBorussiaDortmund = 0; var idBenfica = 0; var idSevillaFC = 0; var idValenciaCF = 0
        var idLiverpoolFC = 0; var idArsenal = 0; var idPorto = 0; var idRiverPlate = 0
        var idAjax = 0; var idBellingham = 0; var idVinicius = 0; var idLewandowski = 0
        var idPedri = 0; var idBruno = 0; var idRashford = 0; var idVlahovic = 0
        var idMbappe = 0; var idMorata = 0; var idJugadorLibre = 0; var idKane = 0
        var idMusiala = 0; var idReus = 0; var idBrandt = 0; var idJoaoMario = 0
        var idModric = 0; var idRodrygo = 0; var idGavi = 0; var idRaphinha = 0
        var idSalah = 0; var idNunez = 0; var idOdegaard = 0; var idSaka = 0
        var idDiMaria = 0; var idTaremi = 0; var idPepe = 0; var idBorja = 0
        var idNachoFernandez = 0; var idBrobbey = 0; var idBergwijn = 0; var idEnNesyri = 0
        var idSuso = 0; var idHugoDuro = 0; var idJaviGuerra = 0; var idJoselu = 0
        var idFerran = 0; var idTrossard = 0; var idGakpo = 0; var idTel = 0
        var idMalen = 0; var idRamosPSG = 0; var idNeres = 0; var idConceicao = 0
        var idBarco = 0; var idAkpom = 0
        // Partidos
        var idPartido1 = 0; var idPartido2 = 0; var idPartido3 = 0; var idPartido4 = 0
        var idPartido5 = 0; var idPartido6 = 0; var idPartido7 = 0; var idPartido8 = 0
        var idPartido9 = 0; var idPartido10 = 0; var idPartido11 = 0; var idPartido12 = 0
        var idPartido13 = 0; var idPartido14 = 0; var idPartido15 = 0; var idPartido16 = 0
        var idPartido17 = 0; var idPartido18 = 0; var idPartido19 = 0; var idPartido20 = 0
        var idPartido21 = 0; var idPartido22 = 0; var idPartido23 = 0; var idPartido24 = 0
        var idPartido25 = 0; var idPartido26 = 0; var idPartido27 = 0; var idPartido28 = 0
        var idPartido29 = 0; var idPartido30 = 0; var idPartido31 = 0; var idPartido32 = 0
        var idPartido33 = 0; var idPartido34 = 0; var idPartido35 = 0; var idPartido36 = 0
        var idPartido37 = 0; var idPartido38 = 0; var idPartido39 = 0; var idPartido40 = 0
        var idPartido41 = 0; var idPartido42 = 0; var idPartido43 = 0; var idPartido44 = 0
        var idPartido45 = 0; var idPartido46 = 0; var idPartido47 = 0; var idPartido48 = 0
        var idPartido49 = 0; var idPartido50 = 0; var idPartido51 = 0; var idPartido52 = 0
        var idPartido53 = 0; var idPartido54 = 0; var idPartido55 = 0
    }
    private suspend fun seedPaises(database: AppDatabase, ids: SeedIds) {
        val dao = database.paisDao()
        ids.idEspana       = dao.insert(PaisEntity(nombre = "España",         bandera = "bandera_espana")).toInt()
        ids.idInglaterra   = dao.insert(PaisEntity(nombre = "Inglaterra",     bandera = "bandera_inglaterra")).toInt()
        ids.idItalia       = dao.insert(PaisEntity(nombre = "Italia",         bandera = "bandera_italia")).toInt()
        ids.idFrancia      = dao.insert(PaisEntity(nombre = "Francia",        bandera = "bandera_francia")).toInt()
        ids.idAlemania     = dao.insert(PaisEntity(nombre = "Alemania",       bandera = "bandera_alemania")).toInt()
        ids.idPortugal     = dao.insert(PaisEntity(nombre = "Portugal",       bandera = "bandera_portugal")).toInt()
        ids.idChampionsLeague = dao.insert(PaisEntity(nombre = "Champions League", bandera = "logo_champions_league")).toInt()
        ids.idArgentina    = dao.insert(PaisEntity(nombre = "Argentina",      bandera = "argentina")).toInt()
        ids.idPaisesBajos  = dao.insert(PaisEntity(nombre = "Países Bajos",   bandera = "paises_bajos")).toInt()
        ids.idBelgica      = dao.insert(PaisEntity(nombre = "Bélgica",        bandera = "bandera_belgica")).toInt()
        ids.idEscocia      = dao.insert(PaisEntity(nombre = "Escocia",        bandera = "bandera_escocia")).toInt()
        ids.idTurquia      = dao.insert(PaisEntity(nombre = "Turquía",        bandera = "bandera_turquia")).toInt()
        ids.idAustria      = dao.insert(PaisEntity(nombre = "Austria",        bandera = "bandera_austria")).toInt()
        ids.idRepCheca     = dao.insert(PaisEntity(nombre = "Rep. Checa",     bandera = "bandera_rep_checa")).toInt()
        ids.idDinamarca    = dao.insert(PaisEntity(nombre = "Dinamarca",      bandera = "bandera_dinamarca")).toInt()
        ids.idSerbia       = dao.insert(PaisEntity(nombre = "Serbia",         bandera = "bandera_serbia")).toInt()
        ids.idUcrania      = dao.insert(PaisEntity(nombre = "Ucrania",        bandera = "bandera_ucrania")).toInt()
    }
    private suspend fun seedLocalidades(database: AppDatabase, ids: SeedIds) {
        val dao = database.localidadDao()
        ids.idMadrid       = dao.insert(LocalidadEntity(nombre = "Madrid",      idPais = ids.idEspana)).toInt()
        ids.idBarcelona    = dao.insert(LocalidadEntity(nombre = "Barcelona",   idPais = ids.idEspana)).toInt()
        ids.idManchester   = dao.insert(LocalidadEntity(nombre = "Manchester",  idPais = ids.idInglaterra)).toInt()
        ids.idTurin        = dao.insert(LocalidadEntity(nombre = "Turín",       idPais = ids.idItalia)).toInt()
        ids.idParis        = dao.insert(LocalidadEntity(nombre = "París",       idPais = ids.idFrancia)).toInt()
        ids.idMunich       = dao.insert(LocalidadEntity(nombre = "Múnich",      idPais = ids.idAlemania)).toInt()
        ids.idDortmund     = dao.insert(LocalidadEntity(nombre = "Dortmund",    idPais = ids.idAlemania)).toInt()
        ids.idLisboa       = dao.insert(LocalidadEntity(nombre = "Lisboa",      idPais = ids.idPortugal)).toInt()
        ids.idOporto       = dao.insert(LocalidadEntity(nombre = "Oporto",      idPais = ids.idPortugal)).toInt()
        ids.idBuenosAires  = dao.insert(LocalidadEntity(nombre = "Buenos Aires",idPais = ids.idArgentina)).toInt()
        ids.idAmsterdam    = dao.insert(LocalidadEntity(nombre = "Ámsterdam",   idPais = ids.idPaisesBajos)).toInt()
        ids.idSeVilla      = dao.insert(LocalidadEntity(nombre = "Sevilla",     idPais = ids.idEspana)).toInt()
        ids.idValencia     = dao.insert(LocalidadEntity(nombre = "Valencia",    idPais = ids.idEspana)).toInt()
        ids.idLondres      = dao.insert(LocalidadEntity(nombre = "Londres",     idPais = ids.idInglaterra)).toInt()
        ids.idLiverpool    = dao.insert(LocalidadEntity(nombre = "Liverpool",     idPais = ids.idInglaterra)).toInt()
        // España
        ids.idBilbao       = dao.insert(LocalidadEntity(nombre = "Bilbao",        idPais = ids.idEspana)).toInt()
        ids.idSanSebastian = dao.insert(LocalidadEntity(nombre = "San Sebastián", idPais = ids.idEspana)).toInt()
        ids.idGetafe       = dao.insert(LocalidadEntity(nombre = "Getafe",        idPais = ids.idEspana)).toInt()
        ids.idPamplona     = dao.insert(LocalidadEntity(nombre = "Pamplona",      idPais = ids.idEspana)).toInt()
        ids.idVitoriaGasteiz = dao.insert(LocalidadEntity(nombre = "Vitoria-Gasteiz", idPais = ids.idEspana)).toInt()
        ids.idVillarreal   = dao.insert(LocalidadEntity(nombre = "Villarreal",    idPais = ids.idEspana)).toInt()
        ids.idGirona       = dao.insert(LocalidadEntity(nombre = "Girona",        idPais = ids.idEspana)).toInt()
        ids.idCadiz        = dao.insert(LocalidadEntity(nombre = "Cádiz",         idPais = ids.idEspana)).toInt()
        ids.idPalmasMallorca = dao.insert(LocalidadEntity(nombre = "Las Palmas",  idPais = ids.idEspana)).toInt()
        ids.idPalmaMallorca = dao.insert(LocalidadEntity(nombre = "Palma",        idPais = ids.idEspana)).toInt()
        ids.idGranada      = dao.insert(LocalidadEntity(nombre = "Granada",       idPais = ids.idEspana)).toInt()
        ids.idAlmeria      = dao.insert(LocalidadEntity(nombre = "Almería",       idPais = ids.idEspana)).toInt()
        ids.idVigo         = dao.insert(LocalidadEntity(nombre = "Vigo",          idPais = ids.idEspana)).toInt()
        // Inglaterra
        ids.idManchester2  = dao.insert(LocalidadEntity(nombre = "Mánchester",    idPais = ids.idInglaterra)).toInt()
        ids.idNewcastle    = dao.insert(LocalidadEntity(nombre = "Newcastle",      idPais = ids.idInglaterra)).toInt()
        // Italia
        ids.idMilan        = dao.insert(LocalidadEntity(nombre = "Milán",          idPais = ids.idItalia)).toInt()
        ids.idNapoles      = dao.insert(LocalidadEntity(nombre = "Nápoles",        idPais = ids.idItalia)).toInt()
        ids.idRoma         = dao.insert(LocalidadEntity(nombre = "Roma",           idPais = ids.idItalia)).toInt()
        // Portugal
        ids.idBraga        = dao.insert(LocalidadEntity(nombre = "Braga",          idPais = ids.idPortugal)).toInt()
        // Países Bajos
        ids.idRotterdam    = dao.insert(LocalidadEntity(nombre = "Róterdam",       idPais = ids.idPaisesBajos)).toInt()
        ids.idEindhoven    = dao.insert(LocalidadEntity(nombre = "Eindhoven",      idPais = ids.idPaisesBajos)).toInt()
        // Bélgica
        ids.idBrujas       = dao.insert(LocalidadEntity(nombre = "Brujas",         idPais = ids.idBelgica)).toInt()
        ids.idBruselas     = dao.insert(LocalidadEntity(nombre = "Bruselas",       idPais = ids.idBelgica)).toInt()
        // Escocia
        ids.idGlasgow      = dao.insert(LocalidadEntity(nombre = "Glasgow",        idPais = ids.idEscocia)).toInt()
        // Turquía
        ids.idEstambul     = dao.insert(LocalidadEntity(nombre = "Estambul",       idPais = ids.idTurquia)).toInt()
        // Austria
        ids.idSalzburg     = dao.insert(LocalidadEntity(nombre = "Salzburgo",      idPais = ids.idAustria)).toInt()
        // Rep. Checa
        ids.idPraga        = dao.insert(LocalidadEntity(nombre = "Praga",          idPais = ids.idRepCheca)).toInt()
        // Dinamarca
        ids.idCopenhague   = dao.insert(LocalidadEntity(nombre = "Copenhague",     idPais = ids.idDinamarca)).toInt()
        // Serbia
        ids.idBelgrado     = dao.insert(LocalidadEntity(nombre = "Belgrado",       idPais = ids.idSerbia)).toInt()
        // Ucrania
        ids.idKharkiv      = dao.insert(LocalidadEntity(nombre = "Járkov",         idPais = ids.idUcrania)).toInt()
    }
    private suspend fun seedEstadios(database: AppDatabase, ids: SeedIds) {
        val dao = database.estadioDao()
        ids.id_1456        = dao.insert(EstadioEntity(nombre = "Estadio Santiago Bernabéu",      idLocalidad = ids.idMadrid,      idPais = ids.idEspana,      capacidad = 81044,  latitud = 40.4531,   longitud = -3.6883,  direccion = "Av. de Concha Espina, 1, Madrid")).toInt()
        ids.id_19939         = dao.insert(EstadioEntity(nombre = "Camp Nou",       idLocalidad = ids.idBarcelona,   idPais = ids.idEspana,      capacidad = 99354,  latitud = 41.3809,   longitud = 2.1228,   direccion = "C. d'Arístides Maillol, Barcelona")).toInt()
        ids.idOldTrafford     = dao.insert(EstadioEntity(nombre = "Old Trafford",           idLocalidad = ids.idManchester,  idPais = ids.idInglaterra,  capacidad = 74140,  latitud = 53.4631,   longitud = -2.2913,  direccion = "Sir Matt Busby Way, Manchester")).toInt()
        ids.id_909         = dao.insert(EstadioEntity(nombre = "Allianz Stadium",        idLocalidad = ids.idTurin,       idPais = ids.idItalia,      capacidad = 41507,  latitud = 45.1096,   longitud = 7.6412,   direccion = "Corso Gaetano Scirea, 50, Turín")).toInt()
        ids.id_671  = dao.insert(EstadioEntity(nombre = "Parc des Princes",       idLocalidad = ids.idParis,       idPais = ids.idFrancia,     capacidad = 47929,  latitud = 48.8414,   longitud = 2.2530,   direccion = "24 Rue du Commandant Guilbaud, París")).toInt()
        ids.idAllianzArena    = dao.insert(EstadioEntity(nombre = "Allianz Arena",          idLocalidad = ids.idMunich,      idPais = ids.idAlemania,    capacidad = 75000,  latitud = 48.2188,   longitud = 11.6247,  direccion = "Werner-Heisenberg-Allee 25, Múnich")).toInt()
        ids.idSignalIduna     = dao.insert(EstadioEntity(nombre = "Signal Iduna Park",      idLocalidad = ids.idDortmund,    idPais = ids.idAlemania,    capacidad = 81365,  latitud = 51.4926,   longitud = 7.4518,   direccion = "Strobelallee 50, Dortmund")).toInt()
        ids.idDaLuz           = dao.insert(EstadioEntity(nombre = "Estádio da Luz",         idLocalidad = ids.idLisboa,      idPais = ids.idPortugal,    capacidad = 64642,  latitud = 38.7525,   longitud = -9.1849,  direccion = "Av. Eusébio da Silva Ferreira, Lisboa")).toInt()
        ids.id_1494  = dao.insert(EstadioEntity(nombre = "Estadio Ramón Sánchez Pizjuán", idLocalidad = ids.idSeVilla,     idPais = ids.idEspana,      capacidad = 43883,  latitud = 37.3840,   longitud = -5.9705,  direccion = "C. Sevilla Fútbol Club, Sevilla")).toInt()
        ids.id_1497        = dao.insert(EstadioEntity(nombre = "Estadio de Mestalla",    idLocalidad = ids.idValencia,    idPais = ids.idEspana,      capacidad = 49430,  latitud = 39.4747,   longitud = -0.3583,  direccion = "C. de Mestalla, 2, Valencia")).toInt()
        ids.id_550         = dao.insert(EstadioEntity(nombre = "Anfield",                idLocalidad = ids.idLiverpool,   idPais = ids.idInglaterra,  capacidad = 61276,  latitud = 53.4308,   longitud = -2.9608,  direccion = "Anfield Rd, Liverpool")).toInt()
        ids.id_494        = dao.insert(EstadioEntity(nombre = "Emirates Stadium",       idLocalidad = ids.idLondres,     idPais = ids.idInglaterra,  capacidad = 60704,  latitud = 51.5549,   longitud = -0.1084,  direccion = "Hornsey Rd, Londres")).toInt()
        ids.idDoDragao        = dao.insert(EstadioEntity(nombre = "Estádio do Dragão",      idLocalidad = ids.idOporto,      idPais = ids.idPortugal,    capacidad = 50033,  latitud = 41.1616,   longitud = -8.5836,  direccion = "Via Futebol Clube do Porto, Oporto")).toInt()
        ids.idMonumental      = dao.insert(EstadioEntity(nombre = "El Monumental",          idLocalidad = ids.idBuenosAires, idPais = ids.idArgentina,   capacidad = 84000,  latitud = -34.5452,  longitud = -58.4497, direccion = "Av. Pres. Figueroa Alcorta 7597, Buenos Aires")).toInt()
        ids.idJohanCruyffArena= dao.insert(EstadioEntity(nombre = "Johan Cruyff Arena",     idLocalidad = ids.idAmsterdam,   idPais = ids.idPaisesBajos, capacidad = 54990,  latitud = 52.3143,   longitud = 4.9412,   direccion = "Arena Blvd 1, Ámsterdam")).toInt()

        // ── LA LIGA 2023-24 — estadios que faltaban ──────────────────────────
        ids.id_19217   = dao.insert(EstadioEntity(nombre = "Estádio Cívitas Metropolitano",       idLocalidad = ids.idMadrid,         idPais = ids.idEspana,      capacidad = 68456,  latitud = 40.4361,   longitud = -3.5994,  direccion = "Av. de Luis Aragonés, s/n, Madrid")).toInt()
        ids.id_1460        = dao.insert(EstadioEntity(nombre = "San Mamés Barria",                   idLocalidad = ids.idBilbao,         idPais = ids.idEspana,      capacidad = 53289,  latitud = 43.2641,   longitud = -2.9494,  direccion = "Rafael Moreno Pitxitxi, s/n, Bilbao")).toInt()
        ids.id_1491      = dao.insert(EstadioEntity(nombre = "Reale Arena",                 idLocalidad = ids.idSanSebastian,   idPais = ids.idEspana,      capacidad = 39500,  latitud = 43.3016,   longitud = -1.9733,  direccion = "Paseo de Anoeta, 1, San Sebastián")).toInt()
        ids.id_1489= dao.insert(EstadioEntity(nombre = "Estadio Benito Villamarín",   idLocalidad = ids.idSeVilla,        idPais = ids.idEspana,      capacidad = 60720,  latitud = 37.3561,   longitud = -5.9811,  direccion = "Av. de Heliópolis, s/n, Sevilla")).toInt()
        ids.idGranCanaria     = dao.insert(EstadioEntity(nombre = "Estadio Gran Canaria",        idLocalidad = ids.idPalmasMallorca, idPais = ids.idEspana,      capacidad = 32400,  latitud = 28.1002,   longitud = -15.4567, direccion = "C. Fondos de Segura, s/n, Las Palmas")).toInt()
        ids.idColiseum        = dao.insert(EstadioEntity(nombre = "Coliseum Alfonso Pérez",      idLocalidad = ids.idGetafe,         idPais = ids.idEspana,      capacidad = 17393,  latitud = 40.3244,   longitud = -3.7141,  direccion = "Av. Teresa de Calcuta, s/n, Getafe")).toInt()
        ids.id_1486         = dao.insert(EstadioEntity(nombre = "Estadio El Sadar",                    idLocalidad = ids.idPamplona,       idPais = ids.idEspana,      capacidad = 23576,  latitud = 42.7966,   longitud = -1.6368,  direccion = "C. Serapio Huici, s/n, Pamplona")).toInt()
        ids.id_1470    = dao.insert(EstadioEntity(nombre = "Estadio de Mendizorroza",     idLocalidad = ids.idVitoriaGasteiz, idPais = ids.idEspana,      capacidad = 19840,  latitud = 42.8370,   longitud = -2.6883,  direccion = "C. Portal de Lasarte, 1, Vitoria-Gasteiz")).toInt()
        ids.id_1498      = dao.insert(EstadioEntity(nombre = "Estadio de la Cerámica",      idLocalidad = ids.idVillarreal,     idPais = ids.idEspana,      capacidad = 23500,  latitud = 39.9444,   longitud = -0.1036,  direccion = "Carrer de la Ceràmica, s/n, Villarreal")).toInt()
        ids.idVallecas        = dao.insert(EstadioEntity(nombre = "Campo de Fútbol de Vallecas", idLocalidad = ids.idMadrid,         idPais = ids.idEspana,      capacidad = 14708,  latitud = 40.3919,   longitud = -3.6590,  direccion = "Av. de la Albufera, 169, Madrid")).toInt()
        ids.idNuevoMirandilla = dao.insert(EstadioEntity(nombre = "Estadio Nuevo Mirandilla",    idLocalidad = ids.idCadiz,          idPais = ids.idEspana,      capacidad = 20724,  latitud = 36.5026,   longitud = -6.2730,  direccion = "C. Acacias, s/n, Cádiz")).toInt()
        ids.idVisitMallorca   = dao.insert(EstadioEntity(nombre = "Visit Mallorca Estadi",       idLocalidad = ids.idPalmaMallorca,  idPais = ids.idEspana,      capacidad = 23142,  latitud = 39.5900,   longitud = 2.6312,   direccion = "C. Gremi Fusters, 11, Palma")).toInt()
        ids.idLosCarmenes     = dao.insert(EstadioEntity(nombre = "Estadio Nuevo Los Cármenes",  idLocalidad = ids.idGranada,        idPais = ids.idEspana,      capacidad = 19336,  latitud = 37.1530,   longitud = -3.5957,  direccion = "C. Arabial, s/n, Granada")).toInt()
        ids.idPowerHorse      = dao.insert(EstadioEntity(nombre = "Power Horse Stadium",         idLocalidad = ids.idAlmeria,        idPais = ids.idEspana,      capacidad = 16135,  latitud = 36.8400,   longitud = -2.4355,  direccion = "C. Padre Méndez, s/n, Almería")).toInt()
        ids.idBalaidos        = dao.insert(EstadioEntity(nombre = "Estadio de Balaídos",         idLocalidad = ids.idVigo,           idPais = ids.idEspana,      capacidad = 29000,  latitud = 42.2119,   longitud = -8.7391,  direccion = "C. Estadi de Balaídos, s/n, Vigo")).toInt()
        ids.idMontilivi       = dao.insert(EstadioEntity(nombre = "Estadio Municipal de Montilivi", idLocalidad = ids.idGirona,      idPais = ids.idEspana,      capacidad = 13450,  latitud = 41.9612,   longitud = 2.8283,   direccion = "C. Migdia, s/n, Girona")).toInt()

        // ── CHAMPIONS LEAGUE 2023-24 — estadios que faltaban ─────────────────
        ids.id_555          = dao.insert(EstadioEntity(nombre = "Etihad Stadium",              idLocalidad = ids.idManchester2,    idPais = ids.idInglaterra,  capacidad = 53400,  latitud = 53.4831,   longitud = -2.2004,  direccion = "Ashton New Rd, Manchester")).toInt()
        ids.id_907         = dao.insert(EstadioEntity(nombre = "Stadio Giuseppe Meazza",      idLocalidad = ids.idMilan,          idPais = ids.idItalia,      capacidad = 75923,  latitud = 45.4781,   longitud = 9.1240,   direccion = "Piazzale Angelo Moratti, Milán")).toInt()
        ids.idMaradona        = dao.insert(EstadioEntity(nombre = "Stadio Diego Armando Maradona", idLocalidad = ids.idNapoles,      idPais = ids.idItalia,      capacidad = 54726,  latitud = 40.8279,   longitud = 14.1931,  direccion = "Via Claudio, 8, Nápoles")).toInt()
        ids.idOlimpicoRoma    = dao.insert(EstadioEntity(nombre = "Stadio Olimpico di Roma",     idLocalidad = ids.idRoma,           idPais = ids.idItalia,      capacidad = 70634,  latitud = 41.9340,   longitud = 12.4547,  direccion = "Viale dei Gladiatori, 2, Roma")).toInt()
        ids.id_1293    = dao.insert(EstadioEntity(nombre = "Estádio José Alvalade",       idLocalidad = ids.idLisboa,         idPais = ids.idPortugal,    capacidad = 50095,  latitud = 38.7613,   longitud = -9.1606,  direccion = "Rua Professor Fernando da Fonseca, Lisboa")).toInt()
        ids.idEstadioMunicipalBraga = dao.insert(EstadioEntity(nombre = "Estádio Municipal de Braga", idLocalidad = ids.idBraga,    idPais = ids.idPortugal,    capacidad = 30154,  latitud = 41.5624,   longitud = -8.4308,  direccion = "Parque Norte, Braga")).toInt()
        ids.idDeKuip          = dao.insert(EstadioEntity(nombre = "De Kuip",                     idLocalidad = ids.idRotterdam,      idPais = ids.idPaisesBajos, capacidad = 51117,  latitud = 51.8942,   longitud = 4.5231,   direccion = "Van Zandvlietplein 1, Róterdam")).toInt()
        ids.id_1143  = dao.insert(EstadioEntity(nombre = "Philips Stadion",             idLocalidad = ids.idEindhoven,      idPais = ids.idPaisesBajos, capacidad = 35000,  latitud = 51.4416,   longitud = 5.4674,   direccion = "Frederiklaan 10A, Eindhoven")).toInt()
        ids.id_176      = dao.insert(EstadioEntity(nombre = "Jan Breydelstadion",         idLocalidad = ids.idBrujas,         idPais = ids.idBelgica,     capacidad = 29062,  latitud = 51.1934,   longitud = 3.1806,   direccion = "Olympialaan 74, Brujas")).toInt()
        ids.idLottoPark       = dao.insert(EstadioEntity(nombre = "Lotto Park",                  idLocalidad = ids.idBruselas,       idPais = ids.idBelgica,     capacidad = 21500,  latitud = 50.8339,   longitud = 4.2981,   direccion = "Brusselsesteenweg 2092, Anderlecht")).toInt()
        ids.id_1386      = dao.insert(EstadioEntity(nombre = "Celtic Park",                 idLocalidad = ids.idGlasgow,        idPais = ids.idEscocia,     capacidad = 60411,  latitud = 55.8499,   longitud = -4.2055,  direccion = "Celtic Park, Glasgow")).toInt()
        ids.id_22761        = dao.insert(EstadioEntity(nombre = "RAMS Park",                   idLocalidad = ids.idEstambul,       idPais = ids.idTurquia,     capacidad = 52600,  latitud = 41.1034,   longitud = 28.9910,  direccion = "Huzur Mah., Sariyer, Estambul")).toInt()
        ids.idSukruSaracoglu  = dao.insert(EstadioEntity(nombre = "Şükrü Saracoğlu Stadyumu",   idLocalidad = ids.idEstambul,       idPais = ids.idTurquia,     capacidad = 50530,  latitud = 40.9877,   longitud = 29.0368,  direccion = "Kadıköy, Estambul")).toInt()
        ids.id_148 = dao.insert(EstadioEntity(nombre = "Red Bull Arena", idLocalidad = ids.idSalzburg,     idPais = ids.idAustria,     capacidad = 29520,  latitud = 47.8164,   longitud = 12.9983,  direccion = "Lieferinger Hauptstraße 1, Salzburgo")).toInt()
        ids.idGeneraliArena   = dao.insert(EstadioEntity(nombre = "Generali Arena",              idLocalidad = ids.idPraga,          idPais = ids.idRepCheca,    capacidad = 19360,  latitud = 50.0997,   longitud = 14.4169,  direccion = "Milady Horákové 98, Praga")).toInt()
        ids.idParkenStadion   = dao.insert(EstadioEntity(nombre = "Parken Stadion",              idLocalidad = ids.idCopenhague,     idPais = ids.idDinamarca,   capacidad = 38065,  latitud = 55.7027,   longitud = 12.5716,  direccion = "Øster Allé 50, Copenhague")).toInt()
        ids.idRajkoMitic      = dao.insert(EstadioEntity(nombre = "Rajko Mitić Stadium",         idLocalidad = ids.idBelgrado,       idPais = ids.idSerbia,      capacidad = 55538,  latitud = 44.7838,   longitud = 20.4625,  direccion = "Ljutice Bogdana 1a, Belgrado")).toInt()
        ids.idShakhtar        = dao.insert(EstadioEntity(nombre = "Metalist Stadium",            idLocalidad = ids.idKharkiv,        idPais = ids.idUcrania,     capacidad = 38633,  latitud = 49.9809,   longitud = 36.2616,  direccion = "Pl. Metalist, 1, Járkov")).toInt()
        ids.idWembley         = dao.insert(EstadioEntity(nombre = "Estadio de Wembley",          idLocalidad = ids.idLondres,        idPais = ids.idInglaterra,  capacidad = 90000,  latitud = 51.5560,   longitud = -0.2796,  direccion = "Wembley, Londres")).toInt()
        ids.idNewcastleStadium = dao.insert(EstadioEntity(nombre = "St. James' Park",            idLocalidad = ids.idNewcastle,      idPais = ids.idInglaterra,  capacidad = 52305,  latitud = 54.9756,   longitud = -1.6217,  direccion = "St. James' Blvd, Newcastle")).toInt()
    }
    private suspend fun seedCompeticionesYTemporadas(database: AppDatabase, ids: SeedIds) {
        val compDao = database.competicionDao()
        ids.idLaLiga       = compDao.insert(CompeticionEntity(nombre = "LaLiga",                       idPais = ids.idEspana)).toInt()
        ids.idPremier      = compDao.insert(CompeticionEntity(nombre = "Premier League",               idPais = ids.idInglaterra)).toInt()
        ids.idChampions    = compDao.insert(CompeticionEntity(nombre = "Champions League",             idPais = ids.idChampionsLeague)).toInt()
        ids.idBundesliga   = compDao.insert(CompeticionEntity(nombre = "Bundesliga",                   idPais = ids.idAlemania)).toInt()
        ids.idPrimeiraLiga = compDao.insert(CompeticionEntity(nombre = "Primeira Liga",                idPais = ids.idPortugal)).toInt()
        ids.idEredivisie   = compDao.insert(CompeticionEntity(nombre = "Eredivisie",                   idPais = ids.idPaisesBajos)).toInt()
        ids.idCopaAmistosa = compDao.insert(CompeticionEntity(nombre = "Copa Amistosa Internacional",  idPais = null)).toInt()

        val tempDao = database.temporadaDao()
        ids.idTemp2526 = tempDao.insert(TemporadaEntity(temporada = "2025/2026")).toInt()
        ids.idTemp2425 = tempDao.insert(TemporadaEntity(temporada = "2024/2025")).toInt()
        ids.idTemp2324 = tempDao.insert(TemporadaEntity(temporada = "2023/2024")).toInt()
        ids.idTemp2223 = tempDao.insert(TemporadaEntity(temporada = "2022/2023")).toInt()
        ids.idTemp2122 = tempDao.insert(TemporadaEntity(temporada = "2021/2022")).toInt()
    }
    private suspend fun seedEquipos(database: AppDatabase, ids: SeedIds) {
        val dao = database.equipoDao()
        ids.idRealMadrid        = dao.insert(EquipoEntity(nombre = "Real Madrid",        anioFundacion = 1902, idEstadio = ids.id_1456,         idLocalidad = ids.idMadrid,      idPais = ids.idEspana,      escudo = "escudo_real_madrid")).toInt()
        ids.idFCBarcelona   = dao.insert(EquipoEntity(nombre = "FC Barcelona",       anioFundacion = 1899, idEstadio = ids.id_19939,          idLocalidad = ids.idBarcelona,   idPais = ids.idEspana,      escudo = "escudo_barcelona")).toInt()
        ids.idManchesterUnited  = dao.insert(EquipoEntity(nombre = "Manchester United",  anioFundacion = 1878, idEstadio = ids.idOldTrafford,      idLocalidad = ids.idManchester,  idPais = ids.idInglaterra,  escudo = "escudo_manchester_united")).toInt()
        ids.idJuventus          = dao.insert(EquipoEntity(nombre = "Juventus",           anioFundacion = 1897, idEstadio = ids.id_909,          idLocalidad = ids.idTurin,       idPais = ids.idItalia,      escudo = "escudo_juventus")).toInt()
        ids.idPSG               = dao.insert(EquipoEntity(nombre = "PSG",                anioFundacion = 1970, idEstadio = ids.id_671,   idLocalidad = ids.idParis,       idPais = ids.idFrancia,     escudo = "escudo_psg")).toInt()
        ids.idEspanaSeleccion   = dao.insert(EquipoEntity(nombre = "España",             anioFundacion = null, idEstadio = null,                   idLocalidad = null,              idPais = ids.idEspana,      escudo = "escudo_seleccion_espana")).toInt()
        ids.idFranciaSeleccion  = dao.insert(EquipoEntity(nombre = "Francia",            anioFundacion = null, idEstadio = null,                   idLocalidad = null,              idPais = ids.idFrancia,     escudo = "escudo_seleccion_francia")).toInt()
        ids.idBayernMunich            = dao.insert(EquipoEntity(nombre = "Bayern Munich",      anioFundacion = 1900, idEstadio = ids.idAllianzArena,     idLocalidad = ids.idMunich,      idPais = ids.idAlemania,    escudo = "escudo_bayern_munich")).toInt()
        ids.idBorussiaDortmund    = dao.insert(EquipoEntity(nombre = "Borussia Dortmund",  anioFundacion = 1909, idEstadio = ids.idSignalIduna,      idLocalidad = ids.idDortmund,    idPais = ids.idAlemania,    escudo = "escudo_borussia_dortmund")).toInt()
        ids.idBenfica           = dao.insert(EquipoEntity(nombre = "Benfica",            anioFundacion = 1904, idEstadio = ids.idDaLuz,            idLocalidad = ids.idLisboa,      idPais = ids.idPortugal,    escudo = "escudo_benfica")).toInt()
        ids.idSevillaFC         = dao.insert(EquipoEntity(nombre = "Sevilla FC",         anioFundacion = 1890, idEstadio = ids.id_1494,   idLocalidad = ids.idSeVilla,     idPais = ids.idEspana,      escudo = "escudo_sevilla")).toInt()
        ids.idValenciaCF        = dao.insert(EquipoEntity(nombre = "Valencia CF",        anioFundacion = 1919, idEstadio = ids.id_1497,         idLocalidad = ids.idValencia,    idPais = ids.idEspana,      escudo = "escudo_valencia")).toInt()
        ids.idLiverpoolFC       = dao.insert(EquipoEntity(nombre = "Liverpool",          anioFundacion = 1892, idEstadio = ids.id_550,          idLocalidad = ids.idLiverpool,   idPais = ids.idInglaterra,  escudo = "escudo_liverpool")).toInt()
        ids.idArsenal           = dao.insert(EquipoEntity(nombre = "Arsenal",            anioFundacion = 1886, idEstadio = ids.id_494,         idLocalidad = ids.idLondres,     idPais = ids.idInglaterra,  escudo = "escudo_arsenal")).toInt()
        ids.idPorto             = dao.insert(EquipoEntity(nombre = "FC Porto",           anioFundacion = 1893, idEstadio = ids.idDoDragao,         idLocalidad = ids.idOporto,      idPais = ids.idPortugal,    escudo = "escudo_porto")).toInt()
        ids.idRiverPlate        = dao.insert(EquipoEntity(nombre = "River Plate",        anioFundacion = 1901, idEstadio = ids.idMonumental,       idLocalidad = ids.idBuenosAires, idPais = ids.idArgentina,   escudo = "escudo_river_plate")).toInt()
        ids.idAjax              = dao.insert(EquipoEntity(nombre = "Ajax",               anioFundacion = 1900, idEstadio = ids.idJohanCruyffArena, idLocalidad = ids.idAmsterdam,   idPais = ids.idPaisesBajos, escudo = "escudo_ajax")).toInt()
    }
    private suspend fun seedJugadores(database: AppDatabase, ids: SeedIds) {
        val dao = database.jugadorDao()
        ids.idBellingham     = dao.insert(JugadorEntity(nombre = "Jude",      apellido1 = "Bellingham",  apellido2 = null, fechaNacimiento = "2003-06-29", idLocalidad = ids.idManchester,  idPais = ids.idInglaterra, idEquipoActual = ids.idRealMadrid,       posicion = "Centrocampista", activo = true)).toInt()
        ids.idVinicius       = dao.insert(JugadorEntity(nombre = "Vinicius",  apellido1 = "Junior",      apellido2 = null, fechaNacimiento = "2000-07-12", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idRealMadrid,       posicion = "Delantero",      activo = true)).toInt()
        ids.idLewandowski    = dao.insert(JugadorEntity(nombre = "Robert",    apellido1 = "Lewandowski", apellido2 = null, fechaNacimiento = "1988-08-21", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idFCBarcelona,  posicion = "Delantero",      activo = true)).toInt()
        ids.idPedri          = dao.insert(JugadorEntity(nombre = "Pedri",     apellido1 = "González",    apellido2 = null, fechaNacimiento = "2002-11-25", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idFCBarcelona,  posicion = "Centrocampista", activo = true)).toInt()
        ids.idBruno          = dao.insert(JugadorEntity(nombre = "Bruno",     apellido1 = "Fernandes",   apellido2 = null, fechaNacimiento = "1994-09-08", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idManchesterUnited, posicion = "Centrocampista", activo = true)).toInt()
        ids.idRashford       = dao.insert(JugadorEntity(nombre = "Marcus",    apellido1 = "Rashford",    apellido2 = null, fechaNacimiento = "1997-10-31", idLocalidad = ids.idManchester,  idPais = ids.idInglaterra, idEquipoActual = ids.idManchesterUnited, posicion = "Delantero",      activo = true)).toInt()
        ids.idVlahovic       = dao.insert(JugadorEntity(nombre = "Dusan",     apellido1 = "Vlahovic",    apellido2 = null, fechaNacimiento = "2000-01-28", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idJuventus,         posicion = "Delantero",      activo = true)).toInt()
        ids.idMbappe         = dao.insert(JugadorEntity(nombre = "Kylian",    apellido1 = "Mbappé",      apellido2 = null, fechaNacimiento = "1998-12-20", idLocalidad = ids.idParis,       idPais = ids.idFrancia,    idEquipoActual = ids.idPSG,              posicion = "Delantero",      activo = true)).toInt()
        ids.idMorata         = dao.insert(JugadorEntity(nombre = "Álvaro",    apellido1 = "Morata",      apellido2 = null, fechaNacimiento = "1992-10-23", idLocalidad = ids.idMadrid,      idPais = ids.idEspana,     idEquipoActual = ids.idEspanaSeleccion,  posicion = "Delantero",      activo = true)).toInt()
        ids.idJugadorLibre   = dao.insert(JugadorEntity(nombre = "Sergio",    apellido1 = "Libre",       apellido2 = null, fechaNacimiento = "1995-05-10", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = null,                   posicion = "Defensa",        activo = true)).toInt()
        ids.idKane           = dao.insert(JugadorEntity(nombre = "Harry",     apellido1 = "Kane",        apellido2 = null, fechaNacimiento = "1993-07-28", idLocalidad = null,              idPais = ids.idInglaterra, idEquipoActual = ids.idBayernMunich,           posicion = "Delantero",      activo = true)).toInt()
        ids.idMusiala        = dao.insert(JugadorEntity(nombre = "Jamal",     apellido1 = "Musiala",     apellido2 = null, fechaNacimiento = "2003-02-26", idLocalidad = null,              idPais = ids.idAlemania,   idEquipoActual = ids.idBayernMunich,           posicion = "Centrocampista", activo = true)).toInt()
        ids.idReus           = dao.insert(JugadorEntity(nombre = "Marco",     apellido1 = "Reus",        apellido2 = null, fechaNacimiento = "1989-05-31", idLocalidad = ids.idDortmund,    idPais = ids.idAlemania,   idEquipoActual = ids.idBorussiaDortmund,   posicion = "Centrocampista", activo = true)).toInt()
        ids.idBrandt         = dao.insert(JugadorEntity(nombre = "Julian",    apellido1 = "Brandt",      apellido2 = null, fechaNacimiento = "1996-05-02", idLocalidad = null,              idPais = ids.idAlemania,   idEquipoActual = ids.idBorussiaDortmund,   posicion = "Centrocampista", activo = true)).toInt()
        ids.idJoaoMario      = dao.insert(JugadorEntity(nombre = "Joao",      apellido1 = "Mario",       apellido2 = null, fechaNacimiento = "1993-01-19", idLocalidad = ids.idLisboa,      idPais = ids.idPortugal,   idEquipoActual = ids.idBenfica,          posicion = "Centrocampista", activo = true)).toInt()
        ids.idModric         = dao.insert(JugadorEntity(nombre = "Luka",      apellido1 = "Modric",      apellido2 = null, fechaNacimiento = "1985-09-09", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idRealMadrid,       posicion = "Centrocampista", activo = true)).toInt()
        ids.idRodrygo        = dao.insert(JugadorEntity(nombre = "Rodrygo",   apellido1 = "Goes",        apellido2 = null, fechaNacimiento = "2001-01-09", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idRealMadrid,       posicion = "Delantero",      activo = true)).toInt()
        ids.idGavi           = dao.insert(JugadorEntity(nombre = "Gavi",      apellido1 = "Páez",        apellido2 = null, fechaNacimiento = "2004-08-05", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idFCBarcelona,  posicion = "Centrocampista", activo = true)).toInt()
        ids.idRaphinha       = dao.insert(JugadorEntity(nombre = "Raphinha",  apellido1 = "Dias",        apellido2 = null, fechaNacimiento = "1996-12-14", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idFCBarcelona,  posicion = "Extremo",        activo = true)).toInt()
        ids.idSalah          = dao.insert(JugadorEntity(nombre = "Mohamed",   apellido1 = "Salah",       apellido2 = null, fechaNacimiento = "1992-06-15", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idLiverpoolFC,      posicion = "Delantero",      activo = true)).toInt()
        ids.idNunez          = dao.insert(JugadorEntity(nombre = "Darwin",    apellido1 = "Núñez",       apellido2 = null, fechaNacimiento = "1999-06-24", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idLiverpoolFC,      posicion = "Delantero",      activo = true)).toInt()
        ids.idOdegaard       = dao.insert(JugadorEntity(nombre = "Martin",    apellido1 = "Odegaard",    apellido2 = null, fechaNacimiento = "1998-12-17", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idArsenal,          posicion = "Centrocampista", activo = true)).toInt()
        ids.idSaka           = dao.insert(JugadorEntity(nombre = "Bukayo",    apellido1 = "Saka",        apellido2 = null, fechaNacimiento = "2001-09-05", idLocalidad = null,              idPais = ids.idInglaterra, idEquipoActual = ids.idArsenal,          posicion = "Extremo",        activo = true)).toInt()
        ids.idDiMaria        = dao.insert(JugadorEntity(nombre = "Ángel",     apellido1 = "Di María",    apellido2 = null, fechaNacimiento = "1988-02-14", idLocalidad = ids.idBuenosAires, idPais = ids.idArgentina,  idEquipoActual = ids.idBenfica,          posicion = "Extremo",        activo = true)).toInt()
        ids.idTaremi         = dao.insert(JugadorEntity(nombre = "Mehdi",     apellido1 = "Taremi",      apellido2 = null, fechaNacimiento = "1992-07-18", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idPorto,            posicion = "Delantero",      activo = true)).toInt()
        ids.idPepe           = dao.insert(JugadorEntity(nombre = "Pepe",      apellido1 = "Pepe",        apellido2 = null, fechaNacimiento = "1983-02-26", idLocalidad = null,              idPais = ids.idPortugal,   idEquipoActual = ids.idPorto,            posicion = "Defensa",        activo = true)).toInt()
        ids.idBorja          = dao.insert(JugadorEntity(nombre = "Miguel",    apellido1 = "Borja",       apellido2 = null, fechaNacimiento = "1993-01-26", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idRiverPlate,       posicion = "Delantero",      activo = true)).toInt()
        ids.idNachoFernandez = dao.insert(JugadorEntity(nombre = "Ignacio",   apellido1 = "Fernández",   apellido2 = null, fechaNacimiento = "1990-01-12", idLocalidad = ids.idBuenosAires, idPais = ids.idArgentina,  idEquipoActual = ids.idRiverPlate,       posicion = "Centrocampista", activo = true)).toInt()
        ids.idBrobbey        = dao.insert(JugadorEntity(nombre = "Brian",     apellido1 = "Brobbey",     apellido2 = null, fechaNacimiento = "2002-02-01", idLocalidad = null,              idPais = ids.idPaisesBajos,idEquipoActual = ids.idAjax,             posicion = "Delantero",      activo = true)).toInt()
        ids.idBergwijn       = dao.insert(JugadorEntity(nombre = "Steven",    apellido1 = "Bergwijn",    apellido2 = null, fechaNacimiento = "1997-10-08", idLocalidad = null,              idPais = ids.idPaisesBajos,idEquipoActual = ids.idAjax,             posicion = "Extremo",        activo = true)).toInt()
        ids.idEnNesyri       = dao.insert(JugadorEntity(nombre = "Youssef",   apellido1 = "En-Nesyri",   apellido2 = null, fechaNacimiento = "1997-06-01", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idSevillaFC,        posicion = "Delantero",      activo = true)).toInt()
        ids.idSuso           = dao.insert(JugadorEntity(nombre = "Suso",      apellido1 = "Fernández",   apellido2 = null, fechaNacimiento = "1993-11-19", idLocalidad = ids.idSeVilla,     idPais = ids.idEspana,     idEquipoActual = ids.idSevillaFC,        posicion = "Extremo",        activo = true)).toInt()
        ids.idHugoDuro       = dao.insert(JugadorEntity(nombre = "Hugo",      apellido1 = "Duro",        apellido2 = null, fechaNacimiento = "1999-11-10", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idValenciaCF,       posicion = "Delantero",      activo = true)).toInt()
        ids.idJaviGuerra     = dao.insert(JugadorEntity(nombre = "Javi",      apellido1 = "Guerra",      apellido2 = null, fechaNacimiento = "2003-05-13", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idValenciaCF,       posicion = "Centrocampista", activo = true)).toInt()
        ids.idJoselu         = dao.insert(JugadorEntity(nombre = "Joselu",    apellido1 = "Mato",        apellido2 = null, fechaNacimiento = "1990-03-27", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idRealMadrid,       posicion = "Delantero",      activo = true)).toInt()
        ids.idFerran         = dao.insert(JugadorEntity(nombre = "Ferran",    apellido1 = "Torres",      apellido2 = null, fechaNacimiento = "2000-02-29", idLocalidad = null,              idPais = ids.idEspana,     idEquipoActual = ids.idFCBarcelona,  posicion = "Delantero",      activo = true)).toInt()
        ids.idTrossard       = dao.insert(JugadorEntity(nombre = "Leandro",   apellido1 = "Trossard",    apellido2 = null, fechaNacimiento = "1994-12-04", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idArsenal,          posicion = "Extremo",        activo = true)).toInt()
        ids.idGakpo          = dao.insert(JugadorEntity(nombre = "Cody",      apellido1 = "Gakpo",       apellido2 = null, fechaNacimiento = "1999-05-07", idLocalidad = null,              idPais = ids.idPaisesBajos,idEquipoActual = ids.idLiverpoolFC,      posicion = "Delantero",      activo = true)).toInt()
        ids.idTel            = dao.insert(JugadorEntity(nombre = "Mathys",    apellido1 = "Tel",         apellido2 = null, fechaNacimiento = "2005-04-27", idLocalidad = null,              idPais = ids.idFrancia,    idEquipoActual = ids.idBayernMunich,           posicion = "Delantero",      activo = true)).toInt()
        ids.idMalen          = dao.insert(JugadorEntity(nombre = "Donyell",   apellido1 = "Malen",       apellido2 = null, fechaNacimiento = "1999-01-19", idLocalidad = null,              idPais = ids.idPaisesBajos,idEquipoActual = ids.idBorussiaDortmund,   posicion = "Delantero",      activo = true)).toInt()
        ids.idRamosPSG       = dao.insert(JugadorEntity(nombre = "Gonçalo",   apellido1 = "Ramos",       apellido2 = null, fechaNacimiento = "2001-06-20", idLocalidad = null,              idPais = ids.idPortugal,   idEquipoActual = ids.idPSG,              posicion = "Delantero",      activo = true)).toInt()
        ids.idNeres          = dao.insert(JugadorEntity(nombre = "David",     apellido1 = "Neres",       apellido2 = null, fechaNacimiento = "1997-03-03", idLocalidad = null,              idPais = null,             idEquipoActual = ids.idBenfica,          posicion = "Extremo",        activo = true)).toInt()
        ids.idConceicao      = dao.insert(JugadorEntity(nombre = "Francisco", apellido1 = "Conceição",   apellido2 = null, fechaNacimiento = "2002-12-14", idLocalidad = null,              idPais = ids.idPortugal,   idEquipoActual = ids.idPorto,            posicion = "Extremo",        activo = true)).toInt()
        ids.idBarco          = dao.insert(JugadorEntity(nombre = "Esequiel",  apellido1 = "Barco",       apellido2 = null, fechaNacimiento = "1999-03-29", idLocalidad = null,              idPais = ids.idArgentina,  idEquipoActual = ids.idRiverPlate,       posicion = "Centrocampista", activo = true)).toInt()
        ids.idAkpom          = dao.insert(JugadorEntity(nombre = "Chuba",     apellido1 = "Akpom",       apellido2 = null, fechaNacimiento = "1995-10-09", idLocalidad = null,              idPais = ids.idInglaterra, idEquipoActual = ids.idAjax,             posicion = "Delantero",      activo = true)).toInt()
    }
    private suspend fun seedPartidos(database: AppDatabase, ids: SeedIds) {
        val dao = database.partidoDao()
        ids.idPartido1  = dao.insert(PartidoEntity(idEquipoLocal = ids.idRealMadrid,       idEquipoVisitante = ids.idFCBarcelona,  golesLocal = 2, golesVisitante = 1, fecha = "2024-04-21", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_1456,         jornada = "Jornada 32")).toInt()
        ids.idPartido2  = dao.insert(PartidoEntity(idEquipoLocal = ids.idManchesterUnited, idEquipoVisitante = ids.idJuventus,         golesLocal = 1, golesVisitante = 1, fecha = "2024-03-12", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.idOldTrafford,      jornada = "Octavos")).toInt()
        ids.idPartido3  = dao.insert(PartidoEntity(idEquipoLocal = ids.idPSG,              idEquipoVisitante = ids.idFCBarcelona,  golesLocal = 2, golesVisitante = 3, fecha = "2024-04-10", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_671,   jornada = "Cuartos")).toInt()
        ids.idPartido4  = dao.insert(PartidoEntity(idEquipoLocal = ids.idEspanaSeleccion,  idEquipoVisitante = ids.idFranciaSeleccion, golesLocal = 2, golesVisitante = 0, fecha = "2024-06-15", idTemporada = ids.idTemp2324, idCompeticion = null,               idEstadio = ids.id_1456,         jornada = "Amistoso")).toInt()
        ids.idPartido5  = dao.insert(PartidoEntity(idEquipoLocal = ids.idRealMadrid,       idEquipoVisitante = ids.idSevillaFC,        golesLocal = 3, golesVisitante = 1, fecha = "2023-09-02", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_1456,         jornada = "Jornada 4")).toInt()
        ids.idPartido6  = dao.insert(PartidoEntity(idEquipoLocal = ids.idFCBarcelona,  idEquipoVisitante = ids.idValenciaCF,       golesLocal = 2, golesVisitante = 0, fecha = "2023-09-16", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_19939,          jornada = "Jornada 5")).toInt()
        ids.idPartido7  = dao.insert(PartidoEntity(idEquipoLocal = ids.idSevillaFC,        idEquipoVisitante = ids.idValenciaCF,       golesLocal = 1, golesVisitante = 1, fecha = "2023-10-01", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_1494,   jornada = "Jornada 8")).toInt()
        ids.idPartido8  = dao.insert(PartidoEntity(idEquipoLocal = ids.idValenciaCF,       idEquipoVisitante = ids.idRealMadrid,       golesLocal = 1, golesVisitante = 2, fecha = "2023-11-11", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_1497,         jornada = "Jornada 13")).toInt()
        ids.idPartido9  = dao.insert(PartidoEntity(idEquipoLocal = ids.idFCBarcelona,  idEquipoVisitante = ids.idSevillaFC,        golesLocal = 3, golesVisitante = 2, fecha = "2024-01-21", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_19939,          jornada = "Jornada 21")).toInt()
        ids.idPartido10 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRealMadrid,       idEquipoVisitante = ids.idFCBarcelona,  golesLocal = 2, golesVisitante = 2, fecha = "2024-03-03", idTemporada = ids.idTemp2324, idCompeticion = ids.idLaLiga,       idEstadio = ids.id_1456,         jornada = "Jornada 27")).toInt()
        ids.idPartido11 = dao.insert(PartidoEntity(idEquipoLocal = ids.idLiverpoolFC,      idEquipoVisitante = ids.idArsenal,          golesLocal = 2, golesVisitante = 1, fecha = "2023-09-24", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.id_550,          jornada = "Matchday 6")).toInt()
        ids.idPartido12 = dao.insert(PartidoEntity(idEquipoLocal = ids.idArsenal,          idEquipoVisitante = ids.idManchesterUnited, golesLocal = 3, golesVisitante = 1, fecha = "2023-10-08", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.id_494,         jornada = "Matchday 8")).toInt()
        ids.idPartido13 = dao.insert(PartidoEntity(idEquipoLocal = ids.idManchesterUnited, idEquipoVisitante = ids.idLiverpoolFC,      golesLocal = 1, golesVisitante = 2, fecha = "2023-12-17", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.idOldTrafford,      jornada = "Matchday 17")).toInt()
        ids.idPartido14 = dao.insert(PartidoEntity(idEquipoLocal = ids.idArsenal,          idEquipoVisitante = ids.idLiverpoolFC,      golesLocal = 1, golesVisitante = 1, fecha = "2024-02-04", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.id_494,         jornada = "Matchday 23")).toInt()
        ids.idPartido15 = dao.insert(PartidoEntity(idEquipoLocal = ids.idLiverpoolFC,      idEquipoVisitante = ids.idManchesterUnited, golesLocal = 3, golesVisitante = 0, fecha = "2024-03-10", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.id_550,          jornada = "Matchday 28")).toInt()
        ids.idPartido16 = dao.insert(PartidoEntity(idEquipoLocal = ids.idManchesterUnited, idEquipoVisitante = ids.idArsenal,          golesLocal = 2, golesVisitante = 2, fecha = "2024-04-21", idTemporada = ids.idTemp2324, idCompeticion = ids.idPremier,      idEstadio = ids.idOldTrafford,      jornada = "Matchday 33")).toInt()
        ids.idPartido17 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBayernMunich,           idEquipoVisitante = ids.idBorussiaDortmund,   golesLocal = 3, golesVisitante = 2, fecha = "2023-09-30", idTemporada = ids.idTemp2324, idCompeticion = ids.idBundesliga,   idEstadio = ids.idAllianzArena,     jornada = "Jornada 6")).toInt()
        ids.idPartido18 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBorussiaDortmund,   idEquipoVisitante = ids.idBayernMunich,           golesLocal = 1, golesVisitante = 1, fecha = "2024-03-30", idTemporada = ids.idTemp2324, idCompeticion = ids.idBundesliga,   idEstadio = ids.idSignalIduna,      jornada = "Jornada 27")).toInt()
        ids.idPartido19 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBayernMunich,           idEquipoVisitante = ids.idAjax,             golesLocal = 2, golesVisitante = 0, fecha = "2024-01-12", idTemporada = ids.idTemp2324, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idAllianzArena,     jornada = "Amistoso")).toInt()
        ids.idPartido20 = dao.insert(PartidoEntity(idEquipoLocal = ids.idAjax,             idEquipoVisitante = ids.idBorussiaDortmund,   golesLocal = 2, golesVisitante = 3, fecha = "2024-01-19", idTemporada = ids.idTemp2324, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idJohanCruyffArena, jornada = "Amistoso")).toInt()
        ids.idPartido21 = dao.insert(PartidoEntity(idEquipoLocal = ids.idAjax,             idEquipoVisitante = ids.idBenfica,          golesLocal = 1, golesVisitante = 2, fecha = "2024-07-20", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idJohanCruyffArena, jornada = "Pretemporada")).toInt()
        ids.idPartido22 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBenfica,          idEquipoVisitante = ids.idPorto,            golesLocal = 2, golesVisitante = 1, fecha = "2023-10-22", idTemporada = ids.idTemp2324, idCompeticion = ids.idPrimeiraLiga, idEstadio = ids.idDaLuz,            jornada = "Jornada 9")).toInt()
        ids.idPartido23 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPorto,            idEquipoVisitante = ids.idBenfica,          golesLocal = 1, golesVisitante = 1, fecha = "2024-03-17", idTemporada = ids.idTemp2324, idCompeticion = ids.idPrimeiraLiga, idEstadio = ids.idDoDragao,         jornada = "Jornada 26")).toInt()
        ids.idPartido24 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBenfica,          idEquipoVisitante = ids.idAjax,             golesLocal = 3, golesVisitante = 1, fecha = "2024-08-11", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idDaLuz,            jornada = "Pretemporada")).toInt()
        ids.idPartido25 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPorto,            idEquipoVisitante = ids.idRiverPlate,       golesLocal = 2, golesVisitante = 2, fecha = "2024-07-28", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idDoDragao,         jornada = "Pretemporada")).toInt()
        ids.idPartido26 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPSG,              idEquipoVisitante = ids.idBenfica,          golesLocal = 3, golesVisitante = 1, fecha = "2023-11-28", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_671,   jornada = "Fase de grupos")).toInt()
        ids.idPartido27 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBenfica,          idEquipoVisitante = ids.idPSG,              golesLocal = 1, golesVisitante = 2, fecha = "2024-02-20", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.idDaLuz,            jornada = "Octavos")).toInt()
        ids.idPartido28 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPSG,              idEquipoVisitante = ids.idLiverpoolFC,      golesLocal = 2, golesVisitante = 2, fecha = "2024-04-09", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_671,   jornada = "Cuartos")).toInt()
        ids.idPartido29 = dao.insert(PartidoEntity(idEquipoLocal = ids.idLiverpoolFC,      idEquipoVisitante = ids.idPSG,              golesLocal = 1, golesVisitante = 0, fecha = "2024-04-17", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_550,          jornada = "Vuelta Cuartos")).toInt()
        ids.idPartido30 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRealMadrid,       idEquipoVisitante = ids.idPSG,              golesLocal = 3, golesVisitante = 2, fecha = "2024-05-08", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_1456,         jornada = "Semifinal")).toInt()
        ids.idPartido31 = dao.insert(PartidoEntity(idEquipoLocal = ids.idArsenal,          idEquipoVisitante = ids.idBayernMunich,           golesLocal = 1, golesVisitante = 1, fecha = "2024-04-10", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.id_494,         jornada = "Cuartos")).toInt()
        ids.idPartido32 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBayernMunich,           idEquipoVisitante = ids.idArsenal,          golesLocal = 2, golesVisitante = 1, fecha = "2024-04-18", idTemporada = ids.idTemp2324, idCompeticion = ids.idChampions,    idEstadio = ids.idAllianzArena,     jornada = "Vuelta Cuartos")).toInt()
        ids.idPartido33 = dao.insert(PartidoEntity(idEquipoLocal = ids.idFCBarcelona,  idEquipoVisitante = ids.idAjax,             golesLocal = 4, golesVisitante = 1, fecha = "2024-09-18", idTemporada = ids.idTemp2425, idCompeticion = ids.idChampions,    idEstadio = ids.id_19939,          jornada = "Fase Liga")).toInt()
        ids.idPartido34 = dao.insert(PartidoEntity(idEquipoLocal = ids.idAjax,             idEquipoVisitante = ids.idManchesterUnited, golesLocal = 2, golesVisitante = 2, fecha = "2024-10-23", idTemporada = ids.idTemp2425, idCompeticion = ids.idChampions,    idEstadio = ids.idJohanCruyffArena, jornada = "Fase Liga")).toInt()
        ids.idPartido35 = dao.insert(PartidoEntity(idEquipoLocal = ids.idManchesterUnited, idEquipoVisitante = ids.idPSG,              golesLocal = 0, golesVisitante = 1, fecha = "2024-11-27", idTemporada = ids.idTemp2425, idCompeticion = ids.idChampions,    idEstadio = ids.idOldTrafford,      jornada = "Fase Liga")).toInt()
        ids.idPartido36 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRiverPlate,       idEquipoVisitante = ids.idAjax,             golesLocal = 2, golesVisitante = 1, fecha = "2023-07-15", idTemporada = ids.idTemp2324, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idMonumental,       jornada = "Amistoso")).toInt()
        ids.idPartido37 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRiverPlate,       idEquipoVisitante = ids.idBenfica,          golesLocal = 2, golesVisitante = 2, fecha = "2022-08-10", idTemporada = ids.idTemp2122, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idMonumental,       jornada = "Amistoso Internacional")).toInt()
        ids.idPartido38 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRiverPlate,       idEquipoVisitante = ids.idPorto,            golesLocal = 1, golesVisitante = 0, fecha = "2025-01-18", idTemporada = ids.idTemp2526, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idMonumental,       jornada = "Copa Invitacional")).toInt()
        ids.idPartido39 = dao.insert(PartidoEntity(idEquipoLocal = ids.idSevillaFC,        idEquipoVisitante = ids.idLiverpoolFC,      golesLocal = 1, golesVisitante = 3, fecha = "2024-07-30", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.id_1494,   jornada = "Pretemporada")).toInt()
        ids.idPartido40 = dao.insert(PartidoEntity(idEquipoLocal = ids.idValenciaCF,       idEquipoVisitante = ids.idArsenal,          golesLocal = 0, golesVisitante = 2, fecha = "2024-08-03", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.id_1497,         jornada = "Pretemporada")).toInt()
        ids.idPartido41 = dao.insert(PartidoEntity(idEquipoLocal = ids.idJuventus,         idEquipoVisitante = ids.idBayernMunich,           golesLocal = 1, golesVisitante = 2, fecha = "2024-08-07", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.id_909,          jornada = "Pretemporada")).toInt()
        ids.idPartido42 = dao.insert(PartidoEntity(idEquipoLocal = ids.idManchesterUnited, idEquipoVisitante = ids.idFCBarcelona,  golesLocal = 2, golesVisitante = 3, fecha = "2024-08-14", idTemporada = ids.idTemp2425, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idOldTrafford,      jornada = "Pretemporada")).toInt()
        ids.idPartido43 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPSG,              idEquipoVisitante = ids.idAjax,             golesLocal = 2, golesVisitante = 0, fecha = "2025-02-12", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.id_671,   jornada = "Playoff")).toInt()
        ids.idPartido44 = dao.insert(PartidoEntity(idEquipoLocal = ids.idAjax,             idEquipoVisitante = ids.idPSG,              golesLocal = 1, golesVisitante = 2, fecha = "2025-02-19", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.idJohanCruyffArena, jornada = "Vuelta Playoff")).toInt()
        ids.idPartido45 = dao.insert(PartidoEntity(idEquipoLocal = ids.idArsenal,          idEquipoVisitante = ids.idBenfica,          golesLocal = 2, golesVisitante = 0, fecha = "2025-03-05", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.id_494,         jornada = "Octavos")).toInt()
        ids.idPartido46 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBenfica,          idEquipoVisitante = ids.idArsenal,          golesLocal = 1, golesVisitante = 1, fecha = "2025-03-12", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.idDaLuz,            jornada = "Vuelta Octavos")).toInt()
        ids.idPartido47 = dao.insert(PartidoEntity(idEquipoLocal = ids.idLiverpoolFC,      idEquipoVisitante = ids.idRealMadrid,       golesLocal = 1, golesVisitante = 2, fecha = "2025-04-09", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.id_550,          jornada = "Cuartos")).toInt()
        ids.idPartido48 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRealMadrid,       idEquipoVisitante = ids.idLiverpoolFC,      golesLocal = 2, golesVisitante = 1, fecha = "2025-04-16", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.id_1456,         jornada = "Vuelta Cuartos")).toInt()
        ids.idPartido49 = dao.insert(PartidoEntity(idEquipoLocal = ids.idFCBarcelona,  idEquipoVisitante = ids.idBayernMunich,           golesLocal = 1, golesVisitante = 3, fecha = "2025-04-10", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.id_19939,          jornada = "Cuartos")).toInt()
        ids.idPartido50 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBayernMunich,           idEquipoVisitante = ids.idFCBarcelona,  golesLocal = 2, golesVisitante = 2, fecha = "2025-04-17", idTemporada = ids.idTemp2526, idCompeticion = ids.idChampions,    idEstadio = ids.idAllianzArena,     jornada = "Vuelta Cuartos")).toInt()
        ids.idPartido51 = dao.insert(PartidoEntity(idEquipoLocal = ids.idSevillaFC,        idEquipoVisitante = ids.idAjax,             golesLocal = 2, golesVisitante = 1, fecha = "2025-07-22", idTemporada = ids.idTemp2526, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.id_1494,   jornada = "Amistoso")).toInt()
        ids.idPartido52 = dao.insert(PartidoEntity(idEquipoLocal = ids.idPorto,            idEquipoVisitante = ids.idValenciaCF,       golesLocal = 1, golesVisitante = 2, fecha = "2025-07-29", idTemporada = ids.idTemp2526, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idDoDragao,         jornada = "Amistoso")).toInt()
        ids.idPartido53 = dao.insert(PartidoEntity(idEquipoLocal = ids.idRiverPlate,       idEquipoVisitante = ids.idManchesterUnited, golesLocal = 0, golesVisitante = 1, fecha = "2025-08-05", idTemporada = ids.idTemp2526, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.idMonumental,       jornada = "Amistoso")).toInt()
        ids.idPartido54 = dao.insert(PartidoEntity(idEquipoLocal = ids.idJuventus,         idEquipoVisitante = ids.idPSG,              golesLocal = 2, golesVisitante = 2, fecha = "2025-08-09", idTemporada = ids.idTemp2526, idCompeticion = ids.idCopaAmistosa, idEstadio = ids.id_909,          jornada = "Amistoso")).toInt()
        ids.idPartido55 = dao.insert(PartidoEntity(idEquipoLocal = ids.idBayernMunich,           idEquipoVisitante = ids.idBorussiaDortmund,   golesLocal = 3, golesVisitante = 2, fecha = "2023-05-15", idTemporada = ids.idTemp2223, idCompeticion = ids.idBundesliga,   idEstadio = ids.idAllianzArena,     jornada = "Jornada 30")).toInt()
    }
    private suspend fun seedPartidoJugadores1(database: AppDatabase, ids: SeedIds) {
        val dao = database.partidoJugadorDao()
        // Partido 1 – Real Madrid vs Barça
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido1, idJugador = ids.idBellingham,  idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido1, idJugador = ids.idVinicius,    idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 85, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido1, idJugador = ids.idLewandowski, idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido1, idJugador = ids.idPedri,       idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 88, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 2 – Manchester United vs Juventus
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido2, idJugador = ids.idBruno,       idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido2, idJugador = ids.idRashford,    idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 82, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido2, idJugador = ids.idVlahovic,    idEquipo = ids.idJuventus,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 3 – PSG vs Barça
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido3, idJugador = ids.idMbappe,      idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido3, idJugador = ids.idLewandowski, idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 4 – España vs Francia
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido4, idJugador = ids.idMorata,      idEquipo = ids.idEspanaSeleccion, titular = true,  minutosJugados = 78, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 5 – Real Madrid vs Sevilla
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idVinicius,    idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 88, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idBellingham,  idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idRodrygo,     idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 82, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idJoselu,      idEquipo = ids.idRealMadrid,      titular = false, minutosJugados = 18, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idEnNesyri,    idEquipo = ids.idSevillaFC,       titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idSuso,        idEquipo = ids.idSevillaFC,       titular = false, minutosJugados = 22, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 5 – entradas que en el original aparecen como partido 5 (Kane/Musiala/Reus)
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idKane,        idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idMusiala,     idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 85, goles = 1, asistencias = 1, amarillas = 0, rojas = 1))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido5, idJugador = ids.idReus,        idEquipo = ids.idBorussiaDortmund,  titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 6 – Barça vs Valencia
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idLewandowski, idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idPedri,       idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 86, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idGavi,        idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 84, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idFerran,      idEquipo = ids.idFCBarcelona, titular = false, minutosJugados = 19, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idJoaoMario,   idEquipo = ids.idBenfica,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 1))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idMbappe,      idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idHugoDuro,    idEquipo = ids.idValenciaCF,      titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido6, idJugador = ids.idJaviGuerra,  idEquipo = ids.idValenciaCF,      titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 10 – Real Madrid vs Barça
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido10, idJugador = ids.idVinicius,    idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido10, idJugador = ids.idBellingham,  idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido10, idJugador = ids.idLewandowski, idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido10, idJugador = ids.idPedri,       idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 89, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido10, idJugador = ids.idFerran,      idEquipo = ids.idFCBarcelona, titular = false, minutosJugados = 25, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 11 – Liverpool vs Arsenal
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido11, idJugador = ids.idSalah,       idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido11, idJugador = ids.idNunez,       idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 83, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido11, idJugador = ids.idGakpo,       idEquipo = ids.idLiverpoolFC,     titular = false, minutosJugados = 17, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido11, idJugador = ids.idOdegaard,    idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido11, idJugador = ids.idSaka,        idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 90, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 12 – Arsenal vs Manchester United
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido12, idJugador = ids.idOdegaard,    idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido12, idJugador = ids.idSaka,        idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 88, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido12, idJugador = ids.idTrossard,    idEquipo = ids.idArsenal,         titular = false, minutosJugados = 20, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido12, idJugador = ids.idBruno,       idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido12, idJugador = ids.idRashford,    idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 85, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
    }
    private suspend fun seedPartidoJugadores2(database: AppDatabase, ids: SeedIds) {
        val dao = database.partidoJugadorDao()
        // Partido 13
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido13, idJugador = ids.idBruno,    idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido13, idJugador = ids.idSalah,    idEquipo = ids.idLiverpoolFC,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido13, idJugador = ids.idNunez,    idEquipo = ids.idLiverpoolFC,      titular = true,  minutosJugados = 84, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 15
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido15, idJugador = ids.idSalah,    idEquipo = ids.idLiverpoolFC,      titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido15, idJugador = ids.idNunez,    idEquipo = ids.idLiverpoolFC,      titular = true,  minutosJugados = 82, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido15, idJugador = ids.idGakpo,    idEquipo = ids.idLiverpoolFC,      titular = false, minutosJugados = 14, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 17 – Bayern vs Dortmund
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idKane,     idEquipo = ids.idBayernMunich,           titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idMusiala,  idEquipo = ids.idBayernMunich,           titular = true,  minutosJugados = 87, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idTel,      idEquipo = ids.idBayernMunich,           titular = false, minutosJugados = 12, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idReus,     idEquipo = ids.idBorussiaDortmund,   titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idBrandt,   idEquipo = ids.idBorussiaDortmund,   titular = true,  minutosJugados = 88, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido17, idJugador = ids.idMalen,    idEquipo = ids.idBorussiaDortmund,   titular = false, minutosJugados = 16, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 18 – Dortmund vs Bayern
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido18, idJugador = ids.idKane,     idEquipo = ids.idBayernMunich,           titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido18, idJugador = ids.idReus,     idEquipo = ids.idBorussiaDortmund,   titular = true,  minutosJugados = 84, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 22 – Benfica vs Porto
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido22, idJugador = ids.idJoaoMario, idEquipo = ids.idBenfica,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido22, idJugador = ids.idDiMaria,  idEquipo = ids.idBenfica,          titular = true,  minutosJugados = 82, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido22, idJugador = ids.idNeres,    idEquipo = ids.idBenfica,          titular = false, minutosJugados = 19, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido22, idJugador = ids.idTaremi,   idEquipo = ids.idPorto,            titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido22, idJugador = ids.idPepe,     idEquipo = ids.idPorto,            titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 23 – Porto vs Benfica
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido23, idJugador = ids.idJoaoMario, idEquipo = ids.idBenfica,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido23, idJugador = ids.idTaremi,   idEquipo = ids.idPorto,            titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido23, idJugador = ids.idConceicao, idEquipo = ids.idPorto,           titular = false, minutosJugados = 21, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 26 – PSG vs Benfica
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido26, idJugador = ids.idMbappe,    idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido26, idJugador = ids.idRamosPSG, idEquipo = ids.idPSG,             titular = false, minutosJugados = 20, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido26, idJugador = ids.idJoaoMario, idEquipo = ids.idBenfica,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 27 – Benfica vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido27, idJugador = ids.idMbappe,    idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido27, idJugador = ids.idRamosPSG, idEquipo = ids.idPSG,             titular = false, minutosJugados = 18, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido27, idJugador = ids.idDiMaria,  idEquipo = ids.idBenfica,          titular = true,  minutosJugados = 80, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 28 – PSG vs Liverpool
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido28, idJugador = ids.idMbappe,    idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido28, idJugador = ids.idSalah,     idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido28, idJugador = ids.idNunez,     idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 86, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 29 – Liverpool vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido29, idJugador = ids.idSalah,     idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido29, idJugador = ids.idMbappe,    idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 30 – Real Madrid vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido30, idJugador = ids.idVinicius,   idEquipo = ids.idRealMadrid,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido30, idJugador = ids.idBellingham, idEquipo = ids.idRealMadrid,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido30, idJugador = ids.idRodrygo,    idEquipo = ids.idRealMadrid,     titular = true,  minutosJugados = 82, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido30, idJugador = ids.idMbappe,     idEquipo = ids.idPSG,            titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 31 – Arsenal vs Bayern
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido31, idJugador = ids.idOdegaard,   idEquipo = ids.idArsenal,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido31, idJugador = ids.idKane,       idEquipo = ids.idBayernMunich,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 32 – Bayern vs Arsenal
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido32, idJugador = ids.idKane,       idEquipo = ids.idBayernMunich,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido32, idJugador = ids.idMusiala,    idEquipo = ids.idBayernMunich,         titular = true,  minutosJugados = 87, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido32, idJugador = ids.idSaka,       idEquipo = ids.idArsenal,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
    }
    private suspend fun seedPartidoJugadores3(database: AppDatabase, ids: SeedIds) {
        val dao = database.partidoJugadorDao()
        // Partido 33 – Barça vs Ajax
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido33, idJugador = ids.idLewandowski, idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido33, idJugador = ids.idPedri,       idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 88, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido33, idJugador = ids.idRaphinha,    idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 82, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido33, idJugador = ids.idFerran,      idEquipo = ids.idFCBarcelona, titular = false, minutosJugados = 16, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido33, idJugador = ids.idBrobbey,     idEquipo = ids.idAjax,            titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 34 – Ajax vs Manchester United
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido34, idJugador = ids.idBrobbey,     idEquipo = ids.idAjax,            titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido34, idJugador = ids.idBergwijn,    idEquipo = ids.idAjax,            titular = true,  minutosJugados = 86, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido34, idJugador = ids.idAkpom,       idEquipo = ids.idAjax,            titular = false, minutosJugados = 20, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido34, idJugador = ids.idBruno,       idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido34, idJugador = ids.idRashford,    idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 88, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 35 – Manchester United vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido35, idJugador = ids.idMbappe,      idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido35, idJugador = ids.idBruno,       idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 36 – River Plate vs Ajax
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido36, idJugador = ids.idBorja,        idEquipo = ids.idRiverPlate,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido36, idJugador = ids.idNachoFernandez, idEquipo = ids.idRiverPlate,  titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido36, idJugador = ids.idBrobbey,      idEquipo = ids.idAjax,          titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 37 – River Plate vs Benfica
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido37, idJugador = ids.idBorja,        idEquipo = ids.idRiverPlate,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido37, idJugador = ids.idNachoFernandez, idEquipo = ids.idRiverPlate,  titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido37, idJugador = ids.idJoaoMario,    idEquipo = ids.idBenfica,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido37, idJugador = ids.idDiMaria,      idEquipo = ids.idBenfica,        titular = true,  minutosJugados = 82, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 38 – River Plate vs Porto
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido38, idJugador = ids.idBorja,        idEquipo = ids.idRiverPlate,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido38, idJugador = ids.idTaremi,       idEquipo = ids.idPorto,          titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 39 – Sevilla vs Liverpool
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido39, idJugador = ids.idEnNesyri,     idEquipo = ids.idSevillaFC,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido39, idJugador = ids.idSalah,        idEquipo = ids.idLiverpoolFC,    titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido39, idJugador = ids.idNunez,        idEquipo = ids.idLiverpoolFC,    titular = true,  minutosJugados = 84, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 40 – Valencia vs Arsenal
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido40, idJugador = ids.idSaka,         idEquipo = ids.idArsenal,        titular = true,  minutosJugados = 86, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido40, idJugador = ids.idOdegaard,     idEquipo = ids.idArsenal,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 41 – Juventus vs Bayern
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido41, idJugador = ids.idVlahovic,     idEquipo = ids.idJuventus,       titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido41, idJugador = ids.idKane,         idEquipo = ids.idBayernMunich,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido41, idJugador = ids.idMusiala,      idEquipo = ids.idBayernMunich,         titular = true,  minutosJugados = 84, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 42 – Manchester United vs Barça
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idBruno,        idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idRashford,     idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 82, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idLewandowski,  idEquipo = ids.idFCBarcelona,  titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idPedri,        idEquipo = ids.idFCBarcelona,  titular = true,  minutosJugados = 88, goles = 0, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idRaphinha,     idEquipo = ids.idFCBarcelona,  titular = true,  minutosJugados = 84, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido42, idJugador = ids.idFerran,       idEquipo = ids.idFCBarcelona,  titular = false, minutosJugados = 21, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 43 – PSG vs Ajax
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido43, idJugador = ids.idMbappe,       idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido43, idJugador = ids.idRamosPSG,     idEquipo = ids.idPSG,             titular = false, minutosJugados = 24, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido43, idJugador = ids.idBrobbey,      idEquipo = ids.idAjax,            titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        // Partido 44 – Ajax vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido44, idJugador = ids.idMbappe,       idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido44, idJugador = ids.idBergwijn,     idEquipo = ids.idAjax,            titular = true,  minutosJugados = 84, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 45 – Arsenal vs Benfica
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido45, idJugador = ids.idOdegaard,     idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido45, idJugador = ids.idSaka,         idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 88, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 46 – Benfica vs Arsenal
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido46, idJugador = ids.idJoaoMario,    idEquipo = ids.idBenfica,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido46, idJugador = ids.idSaka,         idEquipo = ids.idArsenal,         titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 47 – Liverpool vs Real Madrid
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido47, idJugador = ids.idSalah,        idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido47, idJugador = ids.idVinicius,     idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido47, idJugador = ids.idBellingham,   idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 48 – Real Madrid vs Liverpool
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido48, idJugador = ids.idVinicius,     idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido48, idJugador = ids.idRodrygo,      idEquipo = ids.idRealMadrid,      titular = true,  minutosJugados = 84, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido48, idJugador = ids.idSalah,        idEquipo = ids.idLiverpoolFC,     titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 49 – Barça vs Bayern
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido49, idJugador = ids.idLewandowski,  idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido49, idJugador = ids.idKane,         idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido49, idJugador = ids.idMusiala,      idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 86, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 50 – Bayern vs Barça
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido50, idJugador = ids.idLewandowski,  idEquipo = ids.idFCBarcelona, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido50, idJugador = ids.idFerran,       idEquipo = ids.idFCBarcelona, titular = false, minutosJugados = 24, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido50, idJugador = ids.idKane,         idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido50, idJugador = ids.idMusiala,      idEquipo = ids.idBayernMunich,          titular = true,  minutosJugados = 88, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 51 – Sevilla vs Ajax
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido51, idJugador = ids.idEnNesyri,     idEquipo = ids.idSevillaFC,       titular = true,  minutosJugados = 90, goles = 2, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido51, idJugador = ids.idBrobbey,      idEquipo = ids.idAjax,            titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 52 – Porto vs Valencia
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido52, idJugador = ids.idTaremi,       idEquipo = ids.idPorto,           titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido52, idJugador = ids.idHugoDuro,     idEquipo = ids.idValenciaCF,      titular = true,  minutosJugados = 88, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido52, idJugador = ids.idJaviGuerra,   idEquipo = ids.idValenciaCF,      titular = true,  minutosJugados = 90, goles = 1, asistencias = 1, amarillas = 0, rojas = 0))
        // Partido 53 – River Plate vs Manchester United
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido53, idJugador = ids.idBorja,        idEquipo = ids.idRiverPlate,      titular = true,  minutosJugados = 90, goles = 0, asistencias = 0, amarillas = 1, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido53, idJugador = ids.idBruno,        idEquipo = ids.idManchesterUnited, titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        // Partido 54 – Juventus vs PSG
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido54, idJugador = ids.idVlahovic,     idEquipo = ids.idJuventus,        titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido54, idJugador = ids.idMbappe,       idEquipo = ids.idPSG,             titular = true,  minutosJugados = 90, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
        dao.insert(PartidoJugadorEntity(idPartido = ids.idPartido54, idJugador = ids.idRamosPSG,     idEquipo = ids.idPSG,             titular = false, minutosJugados = 23, goles = 1, asistencias = 0, amarillas = 0, rojas = 0))
    }
    private suspend fun seedUsuariosYLogros(database: AppDatabase, ids: SeedIds) {
        val usuarioDao      = database.usuarioDao()
        val logroDao        = database.logroDao()
        val usuarioPartidoDao = database.usuarioPartidoDao()

        val idAdmin = usuarioDao.insert(UsuarioEntity(nombreUsuario = "admin",        email = "admin@futbol.com", passwordHash = "hash_admin_123", fechaRegistro = "2026-03-18", rol = "admin")).toInt()
        val idDemo  = usuarioDao.insert(UsuarioEntity(nombreUsuario = "usuario_demo", email = "demo@futbol.com",  passwordHash = "hash_demo_123",  fechaRegistro = "2026-03-18", rol = "usuario")).toInt()

        val idLogro1 = logroDao.insertLogro(LogroEntity(nombre = "Primer partido guardado", descripcion = "Has guardado tu primer partido")).toInt()
        val idLogro2 = logroDao.insertLogro(LogroEntity(nombre = "Fan del fútbol",          descripcion = "Has consultado varios partidos")).toInt()
        val idLogro3 = logroDao.insertLogro(LogroEntity(nombre = "Coleccionista",            descripcion = "Has guardado más de un partido")).toInt()

        logroDao.asignarLogro(UsuarioLogroEntity(idUsuario = idDemo,  idLogro = idLogro1, fechaObtenido = "2026-03-18"))
        logroDao.asignarLogro(UsuarioLogroEntity(idUsuario = idDemo,  idLogro = idLogro2, fechaObtenido = "2026-03-18"))
        logroDao.asignarLogro(UsuarioLogroEntity(idUsuario = idAdmin, idLogro = idLogro3, fechaObtenido = "2026-03-18"))

        usuarioPartidoDao.insert(UsuarioPartidoEntity(idUsuario = idDemo,  idPartido = ids.idPartido1, fechaRegistro = "2026-03-18"))
        usuarioPartidoDao.insert(UsuarioPartidoEntity(idUsuario = idDemo,  idPartido = ids.idPartido3, fechaRegistro = "2026-03-18"))
        usuarioPartidoDao.insert(UsuarioPartidoEntity(idUsuario = idAdmin, idPartido = ids.idPartido2, fechaRegistro = "2026-03-18"))
    }
}