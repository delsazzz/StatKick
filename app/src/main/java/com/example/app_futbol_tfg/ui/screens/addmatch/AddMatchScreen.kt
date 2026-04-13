package com.example.app_futbol_tfg.ui.screens.addmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary
import com.example.app_futbol_tfg.ui.utils.getDrawableId
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.imePadding
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.style.TextAlign
import com.example.app_futbol_tfg.ui.components.MatchCard
import com.example.app_futbol_tfg.ui.components.MatchSuggestionUi
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
fun AddMatchScreen(db: AppDatabase, onNavigateBottom: (Int) -> Unit, onOpenMatchDetail: (Int) -> Unit)  {
    // Estado visual del buscador.
    var searchText by rememberSaveable { mutableStateOf("") }
    // Filtro que usamos en el buscador por equipos o competición
    var selectedSuggestionType by rememberSaveable { mutableStateOf<String?>(null) }
    var selectedSuggestionId by rememberSaveable { mutableStateOf<Int?>(null) }
    // Esta variable controla si se muestra el desplegable de sugerencias al escribir en el buscador
    var showSuggestions by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    // Estas variables van a traer toda la información desde Room
    val partidos by db.partidoDao().getAll().collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val competiciones by db.competicionDao().getAll().collectAsState(initial = emptyList())
    val temporadas by db.temporadaDao().getAll().collectAsState(initial = emptyList())
    val estadios by db.estadioDao().getAll().collectAsState(initial = emptyList())
    val paises by db.paisDao().getAll().collectAsState(initial = emptyList())

    // Transformamos las listas en mapas clave-valor para que el acceso a los datos sea más eficiente y no recorra listas
    val equiposMap = equipos.associateBy { it.id }
    val competicionesMap = competiciones.associateBy { it.id }
    val temporadasMap = temporadas.associateBy { it.id }
    val estadiosMap = estadios.associateBy { it.id }
    val paisesMap = paises.associateBy { it.id }

    // Convertimos a los equipos en sugerencias visuales para el usuario, transforma datos de Room para UI
    val teamSuggestions = equipos.map { equipo ->
        SearchSuggestionUi.Team(
            id = equipo.id,
            name = equipo.nombre,
            crestRes = getDrawableId(context, equipo.escudo)
        )
    }
    // Convertimos las competiciones en sugerencias visuales para el usuario
    val competitionSuggestions = competiciones.map { competicion ->
        val pais = competicion.idPais?.let { paisesMap[it] }
        SearchSuggestionUi.Competition(
            id = competicion.id,
            name = competicion.nombre,
            flagRes = getDrawableId(context, pais?.bandera)
        )
    }

    val selectedSuggestion: SearchSuggestionUi? = when (selectedSuggestionType) {
        "team" -> teamSuggestions.find { it.id == selectedSuggestionId }
        "competition" -> competitionSuggestions.find { it.id == selectedSuggestionId }
        else -> null
    }

    val matches = partidos.map { partido ->
        val equipoLocal = equiposMap[partido.idEquipoLocal]
        val equipoVisitante = equiposMap[partido.idEquipoVisitante]
        val competicion = partido.idCompeticion?.let { competicionesMap[it] }
        val temporada = partido.idTemporada?.let { temporadasMap[it] }
        val pais = competicion?.idPais?.let { paisesMap[it] }
        MatchSuggestionUi(
            id = partido.id,
            homeTeam = equipoLocal?.nombre ?: "Equipo local",
            homeCrest = getDrawableId(context, equipoLocal?.escudo),
            result = "${partido.golesLocal} - ${partido.golesVisitante}",
            awayTeam = equipoVisitante?.nombre ?: "Equipo visitante",
            awayCrest = getDrawableId(context, equipoVisitante?.escudo),
            date = partido.fecha,
            season = temporada?.temporada ?: "Temporada",
            competition = competicion?.nombre ?: "Competición",
            countryFlag = getDrawableId(context, pais?.bandera)
        )
    }
    // Se normaliza el texto introducido en el buscador a minúsculas y sin espacios de más
    val query = searchText.trim().lowercase()
    // Aplicamos un filtro de sugerencias según lo que escribe el usuario en el buscador
    val filteredSuggestions = if (query.isBlank()) {
        emptyList()
    } else {
        val teams = teamSuggestions.filter {
            it.name.lowercase().contains(query)
        }
        val competitions = competitionSuggestions.filter {
            it.name.lowercase().contains(query)
        }
        // Limitamos las sugerencias al número que queramos para no extender demasiado la lista
        (teams + competitions).take(8)
    }
    // Filtrado de partidos según el texto escrito o la sugerencia seleccionada
    val suggestedMatches = matches
        .sortedByDescending { it.date }
        .take(10)

    val filteredMatches = when (val suggestion = selectedSuggestion) {
        is SearchSuggestionUi.Team -> {
            matches.filter { match ->
                match.homeTeam == suggestion.name || match.awayTeam == suggestion.name
            }
        }
        is SearchSuggestionUi.Competition -> {
            matches.filter { match ->
                match.competition == suggestion.name
            }
        }
        null -> {
            if (query.isBlank()) {
                suggestedMatches
            } else {
                matches.filter { match ->
                    match.homeTeam.lowercase().contains(query) ||
                            match.awayTeam.lowercase().contains(query) ||
                            match.competition.lowercase().contains(query)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Añadir partidos"
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 1,
                onItemSelected = onNavigateBottom
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundLight)
        ) {
            // Variables adaptativas según tamaño del dispositivo
            val isSmallScreen = maxWidth < 360.dp || maxHeight < 700.dp
            val horizontalPadding = if (isSmallScreen) 14.dp else 20.dp
            val sectionSpacing = if (isSmallScreen) 14.dp else 18.dp
            val cardPadding = if (isSmallScreen) 14.dp else 18.dp
            val crestSize = if (isSmallScreen) 28.dp else 34.dp
            val resultSize = if (isSmallScreen) 18.sp else 22.sp
            val teamNameSize = if (isSmallScreen) 14.sp else 16.sp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = 16.dp)
                    .navigationBarsPadding()
                    .imePadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                // Buscador
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        selectedSuggestionType = null
                        selectedSuggestionId = null
                        showSuggestions = it.isNotBlank() // Muestra el desplegable de sugerencias si hay texto escrito
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text("Buscar por equipo o competición")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = RoundedCornerShape(18.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_more),
                            contentDescription = "Buscar",
                            tint = TextSecondary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = TextSecondary,
                        cursorColor = PrimaryBlue,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                // Se muestran sugerencias en un desplegable del buscador según lo que se escriba
                if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            filteredSuggestions.forEach { suggestion ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        // Si pulsamos una sugerencia, se rellena el buscador, se guarda como
                                        // la selección activa y se oculta el desplegable
                                        .clickable {
                                            searchText = when (suggestion) {
                                                is SearchSuggestionUi.Team -> suggestion.name
                                                is SearchSuggestionUi.Competition -> suggestion.name
                                            }

                                            when (suggestion) {
                                                is SearchSuggestionUi.Team -> {
                                                    selectedSuggestionType = "team"
                                                    selectedSuggestionId = suggestion.id
                                                }
                                                is SearchSuggestionUi.Competition -> {
                                                    selectedSuggestionType = "competition"
                                                    selectedSuggestionId = suggestion.id
                                                }
                                            }

                                            showSuggestions = false
                                        }
                                        .padding(horizontal = 14.dp, vertical = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    when (suggestion) {
                                        is SearchSuggestionUi.Team -> {
                                            Image(
                                                painter = painterResource(id = suggestion.crestRes),
                                                contentDescription = suggestion.name,
                                                modifier = Modifier.size(24.dp),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                        is SearchSuggestionUi.Competition -> {
                                            Image(
                                                painter = painterResource(id = suggestion.flagRes),
                                                contentDescription = suggestion.name,
                                                modifier = Modifier.size(24.dp),
                                                contentScale = ContentScale.Fit
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = when (suggestion) {
                                            is SearchSuggestionUi.Team -> suggestion.name
                                            is SearchSuggestionUi.Competition -> suggestion.name
                                        },
                                        color = TextPrimary,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
                val sectionTitle = if (query.isBlank() && selectedSuggestion == null) {
                    "Partidos sugeridos"
                } else {
                    "${filteredMatches.size} partidos encontrados"
                }
                // Título de la sección
                Text(
                    text = sectionTitle,
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                // Se muestra la lista de partidos filtrados según nuestra búsqueda
                filteredMatches.forEach { match ->
                    MatchCard(
                        match = match,
                        crestSize = crestSize,
                        resultSize = resultSize,
                        teamNameSize = teamNameSize,
                        cardPadding = cardPadding,
                        onOpenMatchDetail = onOpenMatchDetail
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

// Modelo de sugerencias que se utiliza en el buscador para equipos y competiciones
// Nos permite mezclar en una misma lista ambos
sealed class SearchSuggestionUi {
    data class Team(
        val id: Int,
        val name: String,
        val crestRes: Int
    ) : SearchSuggestionUi()

    data class Competition(
        val id: Int,
        val name: String,
        val flagRes: Int
    ) : SearchSuggestionUi()
}