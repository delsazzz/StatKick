package com.example.app_futbol_tfg.data

import android.util.Log
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.entity.LogroEntity
import com.example.app_futbol_tfg.data.repository.ApiFootballRepository
import kotlinx.coroutines.delay

// Inicialización global de datos de la aplicación
// Gestiona la carga inicial desde API-Football y la preparación de datos auxiliares
private const val TAG = "AppInitializer"

// Competiciones principales sincronizadas durante la carga inicial
val LIGAS_SELECCIONADAS = listOf(
    2,   // Champions League
    140  // La Liga
)
// Ejecuta la sincronización inicial de datos respetando el orden de dependencias
// entre entidades para mantener la integridad referencial de la base de datos
suspend fun initializeAppData(repo: ApiFootballRepository, apiKey: String, database: AppDatabase) {
    try {
        seedLogros(database)
        // Sincronización inicial de países
        Log.d(TAG, "Iniciando carga de países...")
        repo.fetchAndSaveCountries(apiKey)
        // Sincronización de competiciones controlando el límite de peticiones de la API
        Log.d(TAG, "Iniciando carga de competiciones seleccionadas...")
        LIGAS_SELECCIONADAS.forEach { leagueId ->
            repo.fetchAndSaveLeagueById(apiKey, leagueId)
            delay(300)
        }
        // Sincronización de equipos y estadios asociados
        Log.d(TAG, "Iniciando carga de equipos...")
        LIGAS_SELECCIONADAS.forEach { leagueId ->
            repo.fetchAndSaveTeams(apiKey, leagueId, 2024)
            delay(1500)
        }
        // Actualización manual de coordenadas GPS para estadios
        // API-Football no proporciona esta información de forma consistente
        Log.d(TAG, "Actualizando coordenadas de estadios...")
        actualizarCoordenadasEstadios(repo.db)
        Log.d(TAG, "Carga inicial completada")
    } catch (e: Exception) {
        Log.e(TAG, "Error durante la carga inicial", e)
    }
}
// Inserta los logros predefinidos utilizados por el sistema de gamificación
private suspend fun seedLogros(database: AppDatabase) {
    val logroDao = database.logroDao()
    logroDao.insertLogro(
        LogroEntity(
            id = 1,
            nombre = "Primer partido registrado",
            descripcion = "Has añadido tu primer partido a la aplicación."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 2,
            nombre = "Aficionado en marcha",
            descripcion = "Has registrado al menos 5 partidos."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 3,
            nombre = "Veterano de grada",
            descripcion = "Has registrado al menos 10 partidos."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 4,
            nombre = "Fan del gol",
            descripcion = "Has visto al menos 25 goles."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 5,
            nombre = "Lluvia de goles",
            descripcion = "Has visto al menos 50 goles."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 6,
            nombre = "Coleccionista de equipos",
            descripcion = "Has visto al menos 5 equipos diferentes."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 7,
            nombre = "Plantilla conocida",
            descripcion = "Has visto al menos 10 jugadores diferentes."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 8,
            nombre = "Explorador de estadios",
            descripcion = "Has visitado al menos 3 estadios diferentes."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 9,
            nombre = "Ruta internacional",
            descripcion = "Has registrado partidos de varias competiciones."
        )
    )
    logroDao.insertLogro(
        LogroEntity(
            id = 10,
            nombre = "Partido igualado",
            descripcion = "Has visto al menos un empate."
        )
    )
}
// Completa manualmente información geográfica de estadios almacenados en Room
// El nombre del estadio se utiliza como referencia principal de actualización
private suspend fun actualizarCoordenadasEstadios(db: AppDatabase) {
    val dao = db.estadioDao()
    val estadios = listOf(
        listOf("Centre d'Entrenament de la FAF 1", 42.4976, 1.5076, "Centre d'Entrenament de la FAF, Andorra"),
        listOf("Vazgen Sargsyan anvan Hanrapetakan Marzadasht", 40.1719, 44.5256, "Vazgen Sargsyan Republican Stadium, Ereván"),
        listOf("Red Bull Arena", 51.3457, 12.3483, "Lieferinger Hauptstraße 1, Salzburgo"),
        listOf("Merkur Arena", 51.26164379730468, 6.733049741104227, "Stadionplatz 1, Graz"),
        listOf("Jan Breydelstadion", 51.19325321917653, 3.1800268263117846, "Olympialaan 74, Brujas"),
        listOf("Stade Joseph Mariën", 50.817801928379865, 4.32933879745927, "Chaussée de Bruxelles 223, Forest, Bruselas"),
        listOf("Stadion Maksimir", 45.81870207108569, 16.01800572607896, "Maksimirska cesta 128, Zagreb"),
        listOf("Neo GSP", 35.11453334900989, 33.36290592568616, "Neo GSP Stadium, Nicosia"),
        listOf("MCH Arena", 56.116865510557155, 8.951743855379005, "Kaj Zartows Vej 5, Herning"),
        listOf("Emirates Stadium", 51.55507540104443, -0.10839980250732115, "Hornsey Rd, Londres"),
        listOf("Villa Park", 52.50911167206129, -1.8847720736279472, "Trinity Rd, Birmingham"),
        listOf("Anfield", 53.43083666394454, -2.960815384515897, "Anfield Rd, Liverpool"),
        listOf("Etihad Stadium", 53.483156988041415, -2.2003628735825336, "Ashton New Rd, Manchester"),
        listOf("A. Le Coq Arena", 59.42130261667358, 24.732071897872544, "Asula 4c, Tallin"),
        listOf("Injector Arena", 59.42130261667358, 24.732071897872544, "Tallin, Estonia"),
        listOf("Bolt Arena", 60.18775397870977, 24.922722069076237, "Urheilukatu 5, Helsinki"),
        listOf("Stade Francis-Le Blé", 48.4028789485257, -4.461456702647351, "26 Rue de Quimper, Brest"),
        listOf("Parc des Princes", 48.84143737133335, 2.2530396685361294, "24 Rue du Commandant Guilbaud, París"),
        listOf("BayArena", 51.0381955790848, 7.002256597469253, "Bismarckstraße 122-124, Leverkusen"),
        listOf("Victoria Stadium", 36.14924543579981, -5.350181674280305, "Winston Churchill Ave, Gibraltar"),
        listOf("Stadio Toumbas", 40.614075227031634, 22.971825697040117, "Mikras Asias, Tesalónica"),
        listOf("Groupama Aréna", 47.475391619488995, 19.09525419731295, "Üllői út 129, Budapest"),
        listOf("Víkingsvöllur", 64.11698452782149, -21.85266700188222, "Traðarland 1, Reikiavik"),
        listOf("Tallaght Stadium", 53.283541686567176, -6.373683331262938, "Whitestown Way, Tallaght, Dublín"),
        listOf("Gewiss Stadium", 45.7092236549784, 9.68085912607455, "Viale Giulio Cesare 18, Bérgamo"),
        listOf("Stadio Renato Dall'Ara", 44.49227366435858, 11.309986626025061, "Via Andrea Costa 174, Bolonia"),
        listOf("Stadio Giuseppe Meazza", 45.478073148210015, 9.123995997229537, "Piazzale Angelo Moratti, Milán"),
        listOf("Allianz Stadium", 45.10954459916062, 7.641288426050033, "Corso Gaetano Scirea, 50, Turín"),
        listOf("Stade Parc des Sports", 45.9165547710888, 6.118090226083083, "Rue Pierre de Coubertin, Annecy"),
        listOf("Victor Tedesco Stadium", 35.887290880327846, 14.492337268040119, "Triq Mile End, Hamrun"),
        listOf("Stadionul Orăşenesc",47.76641655993274, 27.933275752072227, "Hîncești, Moldavia"),
        listOf("Stadion Feijenoord", 51.89386447190673, 4.523170039837102, "Van Zandvlietplein 1, Róterdam"),
        listOf("Philips Stadion", 51.441740759193586, 5.467437068652036, "Frederiklaan 10A, Eindhoven"),
        listOf("De Grolsch Veste", 52.23653190161907, 6.837868126359461, "Colosseum 65, Enschede"),
        listOf("Aspmyra Stadion", 67.27661720202403, 14.384345484694174, "Hålogalandsgata 30, Bodø"),
        listOf("Stadion Miejski", 52.39767988607404, 16.8581117552024, "Słoneczna 1, Białystok"),
        listOf("Estádio do Sport Lisboa e Benfica (da Luz)", 38.75270720563245, -9.184734918369115, "Av. Eusébio da Silva Ferreira, Lisboa"),
        listOf("Estádio José Alvalade", 38.76118810800156, -9.160824407829532, "Rua Professor Fernando da Fonseca, Lisboa"),
        listOf("Arena Naţională", 44.437276210505445, 26.152572183693916, "Bulevardul Basarabia 37-39, Bucarest"),
        listOf("Celtic Park", 55.849708450323696, -4.2055480446339555, "Celtic Park, Glasgow"),
        listOf("Ibrox Stadium", 55.85317197312402, -4.309290059975873, "150 Edmiston Dr, Glasgow"),
        listOf("Stadion Rajko Mitić", 44.78322517841204, 20.46493346726023, "Ljutice Bogdana 1a, Belgrado"),
        listOf("Stadion Partizana", 44.78880031693141, 20.459214742675066, "Humska 1, Belgrado"),
        listOf("Estadio Santiago Bernabéu", 40.45301957143828, -3.6883640741303068, "Av. de Concha Espina, 1, Madrid"),
        listOf("San Mamés Barria", 43.264162758372585, -2.9493838316946595, "Rafael Moreno Pitxitxi, s/n, Bilbao"),
        listOf("Estadio Municipal de Butarque", 40.340482649095165, -3.760718889476509, "C. Arquitectura, Leganés"),
        listOf("Abanca-Balaídos", 42.21191212458856, -8.739707816597004, "Av. de Balaídos, Vigo"),
        listOf("Estadio de Mendizorroza", 42.83711704053798, -2.6882617605468524, "C. Portal de Lasarte, 1, Vitoria-Gasteiz"),
        listOf("Estadi Municipal de Montilivi", 41.96114232870088, 2.8284311137286164, "Av. Montilivi 141, Girona"),
        listOf("Estadio de Gran Canaria", 28.100339983476182, -15.456701303350487, "C. Fondos de Segura, Las Palmas"),
        listOf("Estadio El Sadar", 42.79669177115976, -1.6370983452062953, "C. Serapio Huici, s/n, Pamplona"),
        listOf("Estadio de Vallecas", 40.39182230003677, -3.6586082318036004, "Av. de la Albufera 169, Madrid"),
        listOf("Estadio Benito Villamarín", 37.356483644008286, -5.981733160746452, "Av. de Heliópolis, s/n, Sevilla"),
        listOf("Reale Arena", 43.30135115516524, -1.9735975316932126, "Paseo de Anoeta, 1, San Sebastián"),
        listOf("Estadio Municipal José Zorrilla", 41.64461844109449, -4.761252631756954, "Av. Mundial 82, Valladolid"),
        listOf("Estadio Ramón Sánchez Pizjuán", 37.384040465656774, -5.97058563019711, "C. Sevilla Fútbol Club, Sevilla"),
        listOf("Estadio de Mestalla", 39.47464728995925, -0.3583434397842838, "C. de Mestalla, 2, Valencia"),
        listOf("Estadio de la Cerámica", 39.94413303999404, -0.103403373464841, "Carrer de la Ceràmica, s/n, Villarreal"),
        listOf("Eleda Stadion", 55.583739666344556, 12.987760766445167, "Eric Perssons väg 31, Malmö"),
        listOf("Stadio di Cornaredo",46.023767520766384, 8.96147372608747, "Via Trevano, Lugano"),
        listOf("Park Hall Stadium", 52.87591008733704, -3.0264241544177275, "Park Hall, Oswestry"),
        listOf("Gradski Stadion", 42.7849000762829, 18.953689142909987, "Nikšić, Montenegro"),
        listOf("Chele Arena", 41.812816480633295, 41.774495421256525, "Podgorica, Montenegro"),
        listOf("Stadion Tuško Polje", 42.10455327231186, 19.090069759379677, "Tuzi, Montenegro"),
        listOf("Stadyen Dynama", 53.895188172306185, 27.560282019144427, "Kirova 8, Minsk"),
        listOf("Stadion Qajimuqan Muñaytpasov", 42.33536213304662, 69.59352639710482, "Astaná, Kazajistán"),
        listOf("Gradska Plaža Stadium", 44.77602776214579, 17.199539991417854, "Zemun, Belgrado"),
        listOf("Stadion Z'dežele", 46.24643113187769, 15.269985526096702, "Opekarniška cesta 15a, Celje"),
        listOf("Campo Sportivo di Acquaviva", 40.89671818384616, 16.834446279859787, "Acquaviva, San Marino"),
        listOf("Inver Park", 54.850004272766114, -5.827036773517721, "Inver Rd, Larne"),
        listOf("Tofiq Bəhramov adına Respublika stadionu", 40.39731710070247, 49.85242502586773, "Baku, Azerbaiyán"),
        listOf("Stadiumi i qytetit Suharekë", 42.35326358378568, 20.822542025575675, "Suharekë, Kosovo"),
        listOf("Stadion Wankdorf", 46.9631159653141, 7.464831664205551, "Papiermühlestrasse 71, Berna"),
        listOf("Huvepharma Arena", 43.534648027024275, 26.52760366831597, "Razgrad, Bulgaria"),
        listOf("Bloomfield Stadium", 32.0517063936817, 34.761521067921656, "Tel Aviv-Yafo, Israel"),
        listOf("Štadión Tehelné pole", 48.163220052513296, 17.136873226177794, "Viktora Tegelhoffa 4, Bratislava"),
        listOf("Arena Egnatia", 41.076664343012496, 19.661701864701747, "Rrogozhinë, Albania"),
        listOf("Fortuna Arena", 50.06748409146514, 14.47153146988603, "U Slavie 1540/2a, Praga"),
        listOf("Decathlon Arena – Stade Pierre-Mauroy", 50.611899671972694, 3.130462702805473, "261 Bd de Tournai, Villeneuve-d'Ascq"),
        listOf("Estádio Cívitas Metropolitano", 40.43624860348015, -3.5994819029664367, "Av. de Luis Aragonés, s/n, Madrid"),
        listOf("Žemynos progimnazijos stadionas", 55.716706803259164, 24.369593868852963, "Vilna, Lituania"),
        listOf("epet ARENA", 50.09982867153053, 14.415923497427093, "Milady Horákové 98, Praga"),
        listOf("LNK Sporta Parks", 56.90854402680547, 24.15439512574032, "Riga, Letonia"),
        listOf("Camp Nou", 41.38088792112082, 2.122819797068668, "C. d'Arístides Maillol, Barcelona"),
        listOf("Estadi Mallorca Son Moix", 39.58998493991782, 2.630054186101272, "Camí dels Reis, Palma"),
        listOf("Stadion Dynamo im. Valeriy Lobanovskyi", 50.450444623137166, 30.535143261394296, "Hrushevskoho St, Kiev"),
        listOf("NSK Olimpiiskyi", 50.43364156970964, 30.521872239770957, "Velyka Vasylkivska 55, Kiev"),
        listOf("Stage Front Stadium", 41.34790094375395, 2.0755592787712276, "Av. del Baix Llobregat 100, Cornellà de Llobregat"),
        listOf("Estadio Coliseum", 40.32573367145897, -3.714922402970485, "Av. Teresa de Calcuta, Getafe"),
        listOf("Stade Louis-II", 43.72763333621512, 7.415543497159074, "7 Av. des Castelans, Mónaco"),
        listOf("Fußball Arena München", 48.21882406522159, 11.624678068305471, "Werner-Heisenberg-Allee 25, Múnich"),
        listOf("BVB Stadion Dortmund", 51.492588771812954, 7.451835939818775, "Strobelallee 50, Dortmund"),
        listOf("Leipzig Stadium", 51.34573823031554, 12.348266318122702, "Am Sportforum 3, Leipzig"),
        listOf("Stuttgart Arena", 48.792229399071125, 9.232069268533989, "Mercedesstraße 87, Stuttgart"),
        listOf("Chobani Stadyumu Fenerbahçe Şükrü Saracoğlu Spor Kompleksi", 40.987664072687735, 29.036879568218474, "Kadıköy, Estambul"),
        listOf("RAMS Park", 41.10334481800755, 28.99105412589377, "Huzur Mah., Sariyer, Estambul"),
    )
    estadios.forEach { datos ->
        dao.updateCoordenadas(
            nombre    = datos[0] as String,
            latitud   = datos[1] as Double,
            longitud  = datos[2] as Double,
            direccion = datos[3] as String
        )
    }
}