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


@Composable
fun AddMatchScreen(db: AppDatabase, onNavigateBottom: (Int) -> Unit, onOpenMatchDetail: (Int) -> Unit)  {
    // Estado visual del buscador.
    // Más adelante se podrá conectar a filtros reales de Room.
    var searchText by remember { mutableStateOf("") }

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
                matches.forEach { match ->
                    MatchUi(
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
//  Modelo visual temporal para la maqueta de partidos sugeridos.
// Más adelante vendrá de Room o de la fuente real de datos.
data class MatchSuggestionUi(
    val id: Int,
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
private fun MatchUi(
    match: MatchSuggestionUi,
    crestSize: androidx.compose.ui.unit.Dp,
    resultSize: androidx.compose.ui.unit.TextUnit,
    teamNameSize: androidx.compose.ui.unit.TextUnit,
    cardPadding: androidx.compose.ui.unit.Dp,
    onOpenMatchDetail: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onOpenMatchDetail(match.id)
                       },
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