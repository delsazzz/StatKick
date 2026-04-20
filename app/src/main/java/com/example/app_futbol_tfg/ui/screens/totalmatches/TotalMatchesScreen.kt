package com.example.app_futbol_tfg.ui.screens.totalmatches

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.components.MatchCard
import com.example.app_futbol_tfg.ui.components.MatchSuggestionUi
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary

@Composable
fun TotalMatchesScreen(userId: Int, db: AppDatabase, onBack: () -> Unit, onOpenMatchDetail: (Int) -> Unit) {
    val context = LocalContext.current
    // Se recuperan los partidos guardados por el usuario y los datos auxiliares necesarios
    val partidos by db.usuarioPartidoDao().getPartidosByUsuario(userId)
        .collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val competiciones by db.competicionDao().getAll().collectAsState(initial = emptyList())
    val temporadas by db.temporadaDao().getAll().collectAsState(initial = emptyList())
    val paises by db.paisDao().getAll().collectAsState(initial = emptyList())
    // Se transforman las listas en mapas para acceder a cada elemento por id
    val equiposMap = equipos.associateBy { it.id }
    val competicionesMap = competiciones.associateBy { it.id }
    val temporadasMap = temporadas.associateBy { it.id }
    val paisesMap = paises.associateBy { it.id }
    // Se adaptan los partidos a un modelo visual reutilizable por MatchCard
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
            countryFlagUrl = pais?.bandera
        )
    }
    // Los partidos se agrupan por año a partir de la fecha y se ordenan de más reciente a más antiguo
    val groupedMatches = matches
        .groupBy { it.date.take(4) }
        .toSortedMap(compareByDescending { it })
    // Guarda qué años están desplegados en pantalla
    var expandedYears by remember { mutableStateOf(setOf<String>()) }
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Mis partidos guardados",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        containerColor = BackgroundLight
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundLight)
                .safeDrawingPadding()
        ) {
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
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                if (groupedMatches.isEmpty()) {
                    Text(
                        text = "Todavía no tienes partidos guardados.",
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    groupedMatches.forEach { (year, matchesOfYear) ->
                        YearMatchesCard(
                            year = year,
                            matchesOfYear = matchesOfYear,
                            isExpanded = expandedYears.contains(year),
                            onToggleExpanded = {
                                expandedYears = if (expandedYears.contains(year)) {
                                    expandedYears - year
                                } else {
                                    expandedYears + year
                                }
                            },
                            crestSize = crestSize,
                            resultSize = resultSize,
                            teamNameSize = teamNameSize,
                            cardPadding = cardPadding,
                            onOpenMatchDetail = onOpenMatchDetail
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
// Tarjeta que agrupa los partidos de un mismo año y permite desplegarlos o contraerlos
@Composable
private fun YearMatchesCard(
    year: String,
    matchesOfYear: List<MatchSuggestionUi>,
    isExpanded: Boolean,
    onToggleExpanded: () -> Unit,
    crestSize: Dp,
    resultSize: TextUnit,
    teamNameSize: TextUnit,
    cardPadding: Dp,
    onOpenMatchDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggleExpanded() },
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
       Column(
           modifier = Modifier
               .fillMaxWidth()
               .padding(16.dp),
           verticalArrangement = Arrangement.spacedBy(12.dp)
       ) {
           Text(
               text = year,
               color = PrimaryBlue,
               style = MaterialTheme.typography.titleLarge.copy(
                   fontWeight = FontWeight.Bold
               )
           )
           if (isExpanded) {
               matchesOfYear.forEach { match ->
                   MatchCard(
                       match = match,
                       crestSize = crestSize,
                       resultSize = resultSize,
                       teamNameSize = teamNameSize,
                       cardPadding = cardPadding,
                       onOpenMatchDetail = onOpenMatchDetail
                   )
               }
           }
       }
    }
}