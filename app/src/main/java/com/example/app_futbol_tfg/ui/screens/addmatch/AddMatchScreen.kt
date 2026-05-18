package com.example.app_futbol_tfg.ui.screens.addmatch

import android.util.Log
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
import androidx.compose.runtime.getValue
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
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.remember
import com.example.app_futbol_tfg.ui.components.MatchCard
import com.example.app_futbol_tfg.ui.components.MatchSuggestionUi
import androidx.compose.runtime.rememberCoroutineScope
import com.example.app_futbol_tfg.BuildConfig
import com.example.app_futbol_tfg.data.repository.ApiFootballRepositoryProvider
import com.example.app_futbol_tfg.ui.components.ApiImage
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors

@Composable
fun AddMatchScreen(db: AppDatabase, onNavigateBottom: (Int) -> Unit, onOpenMatchDetail: (Int) -> Unit,
                searchText: String, onSearchTextChange: (String) -> Unit, selectedSuggestionType: String?,
                onSelectedSuggestionTypeChange: (String?) -> Unit, selectedSuggestionId: Int?, onSelectedSuggestionIdChange: (Int?) -> Unit,
                showSuggestions: Boolean, onShowSuggestionsChange: (Boolean) -> Unit) {

    val appColors = LocalAppColors.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val apiRepo = remember { ApiFootballRepositoryProvider.getInstance(db) }
    var isLoadingFromApi by remember { mutableStateOf(false) }

    // Aquí se recuperan los datos necesarios desde Room para construir la búsqueda y lista de partidos
    val partidos by db.partidoDao().getAll().collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val competiciones by db.competicionDao().getAll().collectAsState(initial = emptyList())
    val temporadas by db.temporadaDao().getAll().collectAsState(initial = emptyList())
    val paises by db.paisDao().getAll().collectAsState(initial = emptyList())
    // Transformamos las listas en mapas para acceder a los datos por id de forma más eficiente
    val equiposMap = equipos.associateBy { it.id }
    val competicionesMap = competiciones.associateBy { it.id }
    val temporadasMap = temporadas.associateBy { it.id }
    val paisesMap = paises.associateBy { it.id }
    // Convertimos equipos y competiciones en modelos visuales reutilizables para el buscador
    val teamSuggestions = equipos.map { equipo ->
        SearchSuggestionUi.Team(
            id = equipo.id,
            name = equipo.nombre,
            crestUrl = equipo.escudo
        )
    }
    val competitionSuggestions = competiciones.map { competicion ->
        val pais = competicion.idPais?.let { paisesMap[it] }
        SearchSuggestionUi.Competition(
            id = competicion.id,
            name = competicion.nombre,
            logoCompetition = competicion?.logo
        )
    }
    val selectedSuggestion: SearchSuggestionUi? = when (selectedSuggestionType) {
        "team" -> teamSuggestions.find { it.id == selectedSuggestionId }
        "competition" -> competitionSuggestions.find { it.id == selectedSuggestionId }
        else -> null
    }
    // Adaptamos los partidos a un modelo de UI para reutilizar MatchCard
    val matches = partidos.map { partido ->
        val equipoLocal = equiposMap[partido.idEquipoLocal]
        val equipoVisitante = equiposMap[partido.idEquipoVisitante]
        val competicion = partido.idCompeticion?.let { competicionesMap[it] }
        val temporada = partido.idTemporada?.let { temporadasMap[it] }
        val pais = competicion?.idPais?.let { paisesMap[it] }
        MatchSuggestionUi(
            id = partido.id,
            homeTeam = equipoLocal?.nombre ?: "Equipo local",
            homeCrestUrl = equipoLocal?.escudo,
            result = "${partido.golesLocal} - ${partido.golesVisitante}",
            awayTeam = equipoVisitante?.nombre ?: "Equipo visitante",
            awayCrestUrl = equipoVisitante?.escudo,
            date = partido.fecha,
            season = temporada?.temporada ?: "Temporada",
            competition = competicion?.nombre ?: "Competición",
            logoCompetition = competicion?.logo,
            competitionId = competicion?.id
        )
    }
    // Se normaliza el texto introducido en el buscador a minúsculas y sin espacios de más
    val query = searchText.trim().lowercase()
    // Aplicamos un filtro de sugerencias según lo que escribe el usuario en el buscador
    val filteredSuggestions = if (query.isBlank()) {
        emptyList()
    } else {
        val teams = teamSuggestions.filter { it.name.lowercase().contains(query) }
        val competitions = competitionSuggestions.filter { it.name.lowercase().contains(query) }
        // Limitamos las sugerencias al número que queramos para no extender demasiado la lista
        (teams + competitions).take(8)
    }
    // Filtrado de partidos según el texto escrito o la sugerencia seleccionada
    val suggestedMatches = matches
        .sortedByDescending { it.date }
        .take(10)
    // Cuando el usuario selecciona una sugerencia, cargamos los partidos
// desde la API si no los tenemos ya en Room
    val filteredMatches = when (val suggestion = selectedSuggestion) {
        is SearchSuggestionUi.Team -> {
            // Carga bajo demanda de partidos del equipo seleccionado
            LaunchedEffect(suggestion.id) {
                isLoadingFromApi = true
                scope.launch {
                    apiRepo.fetchAndSaveFixturesByTeam(
                        apiKey = BuildConfig.API_FOOTBALL_KEY,
                        teamId = suggestion.id,
                        season = 2024
                    )
                    isLoadingFromApi = false
                }
            }
            matches.filter { match ->
                match.homeTeam == suggestion.name || match.awayTeam == suggestion.name
            }
        }

        is SearchSuggestionUi.Competition -> {
            // Carga bajo demanda de partidos de la competición seleccionada
            LaunchedEffect(suggestion.id) {
                isLoadingFromApi = true
                scope.launch {
                    apiRepo.fetchAndSaveFixturesByLeague(
                        apiKey = BuildConfig.API_FOOTBALL_KEY,
                        leagueId = suggestion.id,
                        season = 2024
                    )
                    isLoadingFromApi = false
                }
            }
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
        containerColor = appColors.background
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(appColors.background)
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
                // Buscador de equipos y competiciones
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        onSearchTextChange(it)
                        onSelectedSuggestionTypeChange(null)
                        onSelectedSuggestionIdChange(null)
                        onShowSuggestionsChange(it.isNotBlank())
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Buscar por equipo o competición") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    shape = RoundedCornerShape(18.dp),
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.nav_more),
                            contentDescription = "Buscar",
                            tint = appColors.textSecondary
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryBlue,
                        unfocusedBorderColor = Color(0xFFCBD5E1),
                        focusedLabelColor = PrimaryBlue,
                        unfocusedLabelColor = appColors.textSecondary,
                        cursorColor = PrimaryBlue,
                        focusedTextColor = appColors.textThird,
                        unfocusedTextColor = appColors.textThird,
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
                // Se muestran sugerencias en un desplegable del buscador según lo que se escriba
                if (showSuggestions && filteredSuggestions.isNotEmpty()) {
                    SuggestionsDropdown(
                        suggestions = filteredSuggestions,
                        onSuggestionSelected = { suggestion ->
                            onSearchTextChange(
                                when (suggestion) {
                                    is SearchSuggestionUi.Team -> suggestion.name
                                    is SearchSuggestionUi.Competition -> suggestion.name
                                }
                            )
                            when (suggestion) {
                                is SearchSuggestionUi.Team -> {
                                    onSelectedSuggestionTypeChange("team")
                                    onSelectedSuggestionIdChange(suggestion.id)
                                }

                                is SearchSuggestionUi.Competition -> {
                                    onSelectedSuggestionTypeChange("competition")
                                    onSelectedSuggestionIdChange(suggestion.id)
                                }
                            }
                            onShowSuggestionsChange(false)
                        }
                    )
                }
                val sectionTitle = if (query.isBlank() && selectedSuggestion == null) {
                    "Partidos sugeridos"
                } else {
                    "${filteredMatches.size} partidos encontrados"
                }
                Text(
                    text = sectionTitle,
                    color = appColors.textPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                )
                // Indicador de carga mientras se obtienen partidos de la API
                if (isLoadingFromApi) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = PrimaryBlue
                        )
                        Text(
                            text = "Buscando partidos...",
                            color = appColors.textSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
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
// Desplegable de sugerencia mostrado bajo el buscador
@Composable
private fun SuggestionsDropdown(suggestions: List<SearchSuggestionUi>, onSuggestionSelected: (SearchSuggestionUi) -> Unit) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            suggestions.forEach { suggestion ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSuggestionSelected(suggestion) }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    when (suggestion) {
                        is SearchSuggestionUi.Team -> {
                            ApiImage(
                                url = suggestion.crestUrl,
                                contentDescription = suggestion.name,
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                        is SearchSuggestionUi.Competition -> {
                            ApiImage(
                                url = suggestion.logoCompetition,
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
                        color = appColors.textThird,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
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
        val crestUrl: String?
    ) : SearchSuggestionUi()
    data class Competition(
        val id: Int,
        val name: String,
        val logoCompetition: String?
    ) : SearchSuggestionUi()
}