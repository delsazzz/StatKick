package com.example.app_futbol_tfg.ui.screens.stats

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import com.example.app_futbol_tfg.ui.components.ApiImage
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors

@Composable
fun StatsScreen(userId: Int, db: AppDatabase, onNavigateBottom: (Int) -> Unit) {
    val appColors = LocalAppColors.current
    val totalPartidos by db.usuarioPartidoDao().countByUsuarioFlow(userId).collectAsState(initial = 0)
    val topEquipos by db.usuarioPartidoDao().getTopEquiposVistos(userId).collectAsState(initial = emptyList())
    val totalEquiposDistintos by db.usuarioPartidoDao().countEquiposDistintosVistos(userId).collectAsState(initial = 0)
    val estadiosVistos by db.usuarioPartidoDao().getEstadiosVistos(userId).collectAsState(initial = emptyList())
    val totalEstadiosDistintos by db.usuarioPartidoDao().countEstadiosDistintosVistos(userId).collectAsState(initial = 0)
    val jugadoresMasVistos by db.partidoJugadorDao().getJugadoresMasVistos(userId).collectAsState(initial = emptyList())
    val totalJugadoresDistintos by db.partidoJugadorDao().countJugadoresDistintosVistos(userId).collectAsState(initial = 0)
    val goleadoresVistos by db.partidoJugadorDao().getTopGoleadoresVistos(userId).collectAsState(initial = emptyList())
    val totalGolesVistos by db.partidoJugadorDao().getTotalGolesVistos(userId).collectAsState(initial = 0)
    val asistentesVistos by db.partidoJugadorDao().getTopAsistentesVistos(userId).collectAsState(initial = emptyList())
    val totalAsistenciasVistas by db.partidoJugadorDao().getTotalAsistenciasVistas(userId).collectAsState(initial = 0)
    val amarillasVistas by db.partidoJugadorDao().getTopAmarillasVistas(userId).collectAsState(initial = emptyList())
    val totalAmarillasVistas by db.partidoJugadorDao().getTotalAmarillasVistas(userId).collectAsState(initial = 0)
    val rojasVistas by db.partidoJugadorDao().getTopRojasVistas(userId).collectAsState(initial = emptyList())
    val totalRojasVistas by db.partidoJugadorDao().getTotalRojasVistas(userId).collectAsState(initial = 0)

    val context = LocalContext.current
    // Se transforman los resultados en modelos visuales reutilizables por la UI.
    val equiposVistosUi = topEquipos.map {
        TeamStatUi(
            name = it.nombre,
            crestUrl = it.escudo,
            matchesCount = it.vecesVisto.toString()
        )
    }
    val jugadoresVistosUi = jugadoresMasVistos.map {
        PlayerStatUi(
            nombre = it.nombre,
            apellido = it.apellido1 ?: "",
            crestUrl = it.escudo,
            flagUrl = it.bandera,
            stat = it.total.toString()
        )
    }
    val goleadoresVistosUi = goleadoresVistos.map {
        PlayerStatUi(
            nombre = it.nombre,
            apellido = it.apellido1 ?: "",
            crestUrl = it.escudo,
            flagUrl = it.bandera,
            stat = it.total.toString()
        )
    }
    val asistentesVistosUi = asistentesVistos.map {
        PlayerStatUi(
            nombre = it.nombre,
            apellido = it.apellido1 ?: "",
            crestUrl = it.escudo,
            flagUrl = it.bandera,
            stat = it.total.toString()
        )
    }
    val amarillasVistasUi = amarillasVistas.map {
        PlayerStatUi(
            nombre = it.nombre,
            apellido = it.apellido1 ?: "",
            crestUrl = it.escudo,
            flagUrl = it.bandera,
            stat = it.total.toString()
        )
    }
    val rojasVistasUi = rojasVistas.map {
        PlayerStatUi(
            nombre = it.nombre,
            apellido = it.apellido1 ?: "",
            crestUrl = it.escudo,
            flagUrl = it.bandera,
            stat = it.total.toString()
        )
    }
    val estadiosVistosUi = estadiosVistos.map {
        StadiumStatUi(
            name = it.nombre,
            iconRes = R.drawable.logo_estadio
        )
    }
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Estadísticas"
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 2,
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
                .safeDrawingPadding()
        ) {
            val isSmallScreen = maxWidth < 360.dp || maxHeight < 700.dp
            val horizontalPadding = if (isSmallScreen) 14.dp else 20.dp
            val sectionSpacing = if (isSmallScreen) 18.dp else 24.dp
            val totalSize = if (isSmallScreen) 26.sp else 34.sp
            val subtitleSize = if (isSmallScreen) 13.sp else 14.sp
            val cardWidth = if (isSmallScreen) 115.dp else 128.dp
            val avatarSize = if (isSmallScreen) 50.dp else 58.dp
            val crestSize = if (isSmallScreen) 22.dp else 26.dp
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = 18.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                StatsSection(
                    title = "Partidos y equipos vistos",
                    totalLabel = "Total de partidos vistos",
                    totalValue = totalPartidos.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    equiposVistosUi.forEach { team ->
                        TeamMiniCard(
                            item = team,
                            cardWidth = cardWidth,
                            crestSize = 38.dp
                        )
                    }
                }
                StatsSection(
                    title = "Jugadores más vistos",
                    totalLabel = "Total de jugadores vistos",
                    totalValue = totalJugadoresDistintos.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    jugadoresVistosUi.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }
                StatsSection(
                    title = "Goleadores vistos",
                    totalLabel = "Total de goles vistos",
                    totalValue = totalGolesVistos.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    goleadoresVistosUi.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }
                StatsSection(
                    title = "Asistencias vistas",
                    totalLabel = "Total de asistencias vistas",
                    totalValue = totalAsistenciasVistas.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    asistentesVistosUi.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }
                StatsSection(
                    title = "Jugadores con más amarillas",
                    totalLabel = "Total de amarillas vistas",
                    totalValue = totalAmarillasVistas.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    amarillasVistasUi.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }
                StatsSection(
                    title = "Jugadores con más rojas",
                    totalLabel = "Total de rojas vistas",
                    totalValue = totalRojasVistas.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    rojasVistasUi.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }
                StatsSection(
                    title = "Estadios vistos",
                    totalLabel = "Total de estadios vistos",
                    totalValue = totalEstadiosDistintos.toString(),
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    estadiosVistosUi.forEach { stadium ->
                        StadiumMiniCard(
                            item = stadium,
                            cardWidth = cardWidth
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
// Sección reutilizable para mostrar un total principal y una fila horizontal de tarjetas
@Composable
private fun StatsSection(
    title: String,
    totalLabel: String,
    totalValue: String,
    totalSize: androidx.compose.ui.unit.TextUnit,
    subtitleSize: androidx.compose.ui.unit.TextUnit,
    content: @Composable () -> Unit
) {
    val appColors = LocalAppColors.current
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = title,
            color = appColors.textPrimary,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = appColors.card),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = totalLabel,
                        color = appColors.textSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = subtitleSize)
                    )
                    Text(
                        text = totalValue,
                        color = PrimaryBlue,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = totalSize,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    content()
                }
            }
        }
    }
}
 // Modelo visual de equipos vistos
data class TeamStatUi(
    val name: String,
    val crestUrl: String?,
    val matchesCount: String
)
// Modelo visual de jugadores
data class PlayerStatUi(
    val nombre: String,
    val apellido: String,
    val crestUrl: String?,
    val flagUrl: String?,
    val stat: String
)
// Modelo visual de estadios.
data class StadiumStatUi(
    val name: String,
    val iconRes: Int
)
// Minicard para equipos con escudo y número de partidos.
@Composable
private fun TeamMiniCard(
    item: TeamStatUi,
    cardWidth: Dp,
    crestSize: Dp
) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.background)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ApiImage(
                url = item.crestUrl,
                contentDescription = item.name,
                modifier = Modifier.size(crestSize),
                contentScale = ContentScale.Fit
            )
            Text(
                text = item.matchesCount,
                color = PrimaryBlue,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = item.name,
                    color = appColors.textPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}
// Tarjeta compacta para jugadores con avatar, escudo y valor estadístico
@Composable
private fun PlayerMiniStatCard(
    item: PlayerStatUi,
    cardWidth: Dp,
    avatarSize: Dp,
    crestSize: Dp
) {
    val appColors = LocalAppColors.current

    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ApiImage(
                url = item.crestUrl,
                contentDescription = "Escudo de ${item.nombre} ${item.apellido}",
                modifier = Modifier.size(avatarSize),
                contentScale = ContentScale.Fit
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (!item.flagUrl.isNullOrBlank()) {
                    ApiImage(
                        url = item.flagUrl,
                        contentDescription = "Bandera de ${item.nombre} ${item.apellido}",
                        modifier = Modifier.size(crestSize),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.profile_user),
                        contentDescription = "Jugador genérico",
                        modifier = Modifier.size(crestSize),
                        contentScale = ContentScale.Fit
                    )
                }

                Text(
                    text = item.stat,
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Text(
                text = "${item.nombre} ${item.apellido}",
                modifier = Modifier.height(42.dp),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                color = appColors.textPrimary,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 14.sp
                )
            )
        }
    }
}
// Tarjeta compacta para estadios vistos
@Composable
private fun StadiumMiniCard(
    item: StadiumStatUi,
    cardWidth: androidx.compose.ui.unit.Dp
) {
    val appColors = LocalAppColors.current
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = appColors.background
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                painter = painterResource(id = item.iconRes),
                contentDescription = item.name,
                tint = PrimaryBlue,
                modifier = Modifier.size(28.dp)
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.height(36.dp)
            ) {
                Text(
                    text = item.name,
                    color = appColors.textPrimary,
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }
    }
}