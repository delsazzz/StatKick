package com.example.app_futbol_tfg.ui.screens.addmatch

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

@Composable
fun AddMatchScreen() {
    // Estado visual del buscador.
    // Más adelante se podrá conectar a filtros reales de Room.
    var searchText by remember { mutableStateOf("") }
    // Lista mock visual de partidos sugeridos.
    // Más adelante esto vendrá de la base de datos.
    val suggestedMatches = listOf(
        MatchSuggestionUi(
            homeTeam = "Getafe",
            homeCrest = R.drawable.escudo_getafe,
            result = "2 - 1",
            awayTeam = "Leganés",
            awayCrest = R.drawable.escudo_psg,
            date = "12/03/2024",
            season = "2023/24",
            competition = "LaLiga EA Sports",
            countryFlag = R.drawable.bandera_espana
        ),
        MatchSuggestionUi(
            homeTeam = "Real Madrid",
            homeCrest = R.drawable.escudo_real_madrid,
            result = "3 - 0",
            awayTeam = "Sevilla",
            awayCrest = R.drawable.escudo_sevilla,
            date = "24/02/2024",
            season = "2023/24",
            competition = "LaLiga EA Sports",
            countryFlag = R.drawable.bandera_espana
        ),
        MatchSuggestionUi(
            homeTeam = "Liverpool",
            homeCrest = R.drawable.escudo_liverpool,
            result = "1 - 1",
            awayTeam = "Arsenal",
            awayCrest = R.drawable.escudo_arsenal,
            date = "05/11/2023",
            season = "2023/24",
            competition = "Premier League",
            countryFlag = R.drawable.bandera_inglaterra
        ),
        MatchSuggestionUi(
            homeTeam = "Juventus",
            homeCrest = R.drawable.escudo_juventus,
            result = "0 - 2",
            awayTeam = "Inter",
            awayCrest = R.drawable.escudo_inter_milan,
            date = "18/01/2024",
            season = "2023/24",
            competition = "Serie A",
            countryFlag = R.drawable.bandera_italia
        ),
        MatchSuggestionUi(
            homeTeam = "PSG",
            homeCrest = R.drawable.escudo_psg,
            result = "4 - 2",
            awayTeam = "Lyon",
            awayCrest = R.drawable.escudo_olympique_lyon,
            date = "09/04/2024",
            season = "2023/24",
            competition = "Ligue 1",
            countryFlag = R.drawable.bandera_francia
        )
    )
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Añadir partidos"
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 1,
                onItemSelected = {
                    // Más adelante: navegación real
                }
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
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                // Buscador
                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
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
                // Título de la sección
                Text(
                    text = "5 partidos sugeridos",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                // Lista de partidos sugeridos
                suggestedMatches.forEach { match ->
                    MatchSuggestionCard(
                        match = match,
                        crestSize = crestSize,
                        resultSize = resultSize,
                        teamNameSize = teamNameSize,
                        cardPadding = cardPadding
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
//  Modelo visual temporal para la maqueta de partidos sugeridos.
// Más adelante vendrá de Room o de la fuente real de datos.
data class MatchSuggestionUi(
    val homeTeam: String,
    val homeCrest: Int,
    val result: String,
    val awayTeam: String,
    val awayCrest: Int,
    val date: String,
    val season: String,
    val competition: String,
    val countryFlag: Int
)
// Card reutilizable de un partido sugerido.
@Composable
private fun MatchSuggestionCard(
    match: MatchSuggestionUi,
    crestSize: androidx.compose.ui.unit.Dp,
    resultSize: androidx.compose.ui.unit.TextUnit,
    teamNameSize: androidx.compose.ui.unit.TextUnit,
    cardPadding: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(cardPadding),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Fila principal del partido
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Equipo local
                Text(
                    text = match.homeTeam,
                    modifier = Modifier.weight(1f),
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = teamNameSize,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Escudo local
                Image(
                    painter = painterResource(id = match.homeCrest),
                    contentDescription = match.homeTeam,
                    modifier = Modifier.size(crestSize),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Resultado
                Text(
                    text = match.result,
                    color = PrimaryBlue,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = resultSize,
                        fontWeight = FontWeight.Bold
                    )
                )
                Spacer(modifier = Modifier.width(12.dp))
                // Escudo visitante
                Image(
                    painter = painterResource(id = match.awayCrest),
                    contentDescription = match.awayTeam,
                    modifier = Modifier.size(crestSize),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(8.dp))
                // Equipo visitante
                Text(
                    text = match.awayTeam,
                    modifier = Modifier.weight(1f),
                    color = TextPrimary,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = teamNameSize,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }
            // Segunda fila con información adicional del partido
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = match.date,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(10.dp))
                DotSeparator()
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = match.season,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(10.dp))
                DotSeparator()
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = match.competition,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(id = match.countryFlag),
                    contentDescription = "País competición",
                    modifier = Modifier.size(18.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

// Separador entre texto con forma de punto ·
@Composable
private fun DotSeparator() {
    Text(
        text = "•",
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )
}