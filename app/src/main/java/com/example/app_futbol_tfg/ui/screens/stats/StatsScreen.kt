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
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

@Composable
fun StatsScreen() {

    // ----------------------------------------------------------------
    // DATOS MOCK VISUALES
    // Esto es solo para maquetar la UI.
    // Más adelante todo esto vendrá de Room / ViewModel.
    // ----------------------------------------------------------------

    val equiposVistos = listOf(
        TeamStatUi("Real Madrid", R.drawable.escudo_real_madrid, "18"),
        TeamStatUi("Getafe", R.drawable.escudo_getafe, "12"),
        TeamStatUi("Arsenal", R.drawable.escudo_arsenal, "9"),
        TeamStatUi("PSG", R.drawable.escudo_psg, "7"),
        TeamStatUi("Juventus", R.drawable.escudo_juventus, "5")
    )

    val jugadoresVistos = listOf(
        PlayerStatUi("Bellingham", R.drawable.escudo_real_madrid),
        PlayerStatUi("Borja Mayoral", R.drawable.escudo_getafe),
        PlayerStatUi("Saka", R.drawable.escudo_arsenal),
        PlayerStatUi("Mbappé", R.drawable.escudo_psg),
        PlayerStatUi("Vlahović", R.drawable.escudo_juventus)
    )

    val goleadoresVistos = listOf(
        PlayerStatUi("Borja Mayoral", R.drawable.escudo_getafe),
        PlayerStatUi("Bellingham", R.drawable.escudo_real_madrid),
        PlayerStatUi("Mbappé", R.drawable.escudo_psg),
        PlayerStatUi("Saka", R.drawable.escudo_arsenal),
        PlayerStatUi("Vlahović", R.drawable.escudo_juventus)
    )

    val asistentesVistos = listOf(
        PlayerStatUi("Kroos", R.drawable.escudo_real_madrid),
        PlayerStatUi("Greenwood", R.drawable.escudo_getafe),
        PlayerStatUi("Ødegaard", R.drawable.escudo_arsenal),
        PlayerStatUi("Dembélé", R.drawable.escudo_psg),
        PlayerStatUi("Chiesa", R.drawable.escudo_juventus)
    )

    val tarjetasVistas = listOf(
        EventStatUi("Amarillas", R.drawable.football_yellow_card, "43"),
        EventStatUi("Rojas", R.drawable.football_red_card, "6")
    )

    val estadiosVistos = listOf(
        StadiumStatUi("Bernabéu", R.drawable.map_pin),
        StadiumStatUi("Coliseum", R.drawable.map_pin),
        StadiumStatUi("Emirates", R.drawable.map_pin),
        StadiumStatUi("Parc des Princes", R.drawable.map_pin),
        StadiumStatUi("Allianz Stadium", R.drawable.map_pin)
    )

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Estadísticas"
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 2,
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
            // ------------------------------------------------------------
            // AJUSTES ADAPTATIVOS
            // ------------------------------------------------------------
            val isSmallScreen = maxWidth < 360.dp || maxHeight < 700.dp

            val horizontalPadding = if (isSmallScreen) 14.dp else 20.dp
            val sectionSpacing = if (isSmallScreen) 18.dp else 24.dp
            val totalSize = if (isSmallScreen) 26.sp else 34.sp
            val subtitleSize = if (isSmallScreen) 13.sp else 14.sp
            val cardWidth = if (isSmallScreen) 100.dp else 112.dp
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

                // ============================================================
                // BLOQUE 1 - PARTIDOS Y EQUIPOS VISTOS
                // ============================================================
                StatsSection(
                    title = "Partidos y equipos vistos",
                    totalLabel = "Partidos vistos",
                    totalValue = "51",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    equiposVistos.forEach { team ->
                        TeamMiniCard(
                            item = team,
                            cardWidth = cardWidth,
                            crestSize = 38.dp
                        )
                    }
                }

                // ============================================================
                // BLOQUE 2 - JUGADORES MÁS VISTOS
                // ============================================================
                StatsSection(
                    title = "Jugadores más vistos",
                    totalLabel = "Jugadores vistos",
                    totalValue = "87",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    jugadoresVistos.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }

                // ============================================================
                // BLOQUE 3 - GOLEADORES VISTOS
                // ============================================================
                StatsSection(
                    title = "Goleadores vistos",
                    totalLabel = "Goles vistos",
                    totalValue = "146",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    goleadoresVistos.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }

                // ============================================================
                // BLOQUE 4 - ASISTENCIAS
                // ============================================================
                StatsSection(
                    title = "Asistencias vistas",
                    totalLabel = "Asistencias",
                    totalValue = "98",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    asistentesVistos.forEach { player ->
                        PlayerMiniStatCard(
                            item = player,
                            cardWidth = cardWidth,
                            avatarSize = avatarSize,
                            crestSize = crestSize
                        )
                    }
                }

                // ============================================================
                // BLOQUE 5 - TARJETAS
                // ============================================================
                StatsSection(
                    title = "Tarjetas vistas",
                    totalLabel = "Tarjetas totales",
                    totalValue = "49",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    tarjetasVistas.forEach { event ->
                        EventMiniCard(
                            item = event,
                            cardWidth = cardWidth
                        )
                    }
                }

                // ============================================================
                // BLOQUE 6 - ESTADIOS
                // ============================================================
                StatsSection(
                    title = "Estadios vistos",
                    totalLabel = "Estadios",
                    totalValue = "11",
                    totalSize = totalSize,
                    subtitleSize = subtitleSize
                ) {
                    estadiosVistos.forEach { stadium ->
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

/**
 * Sección reutilizable de estadísticas.
 * Muestra:
 * - título del bloque
 * - total grande
 * - scroll horizontal de tarjetas
 */
@Composable
private fun StatsSection(
    title: String,
    totalLabel: String,
    totalValue: String,
    totalSize: androidx.compose.ui.unit.TextUnit,
    subtitleSize: androidx.compose.ui.unit.TextUnit,
    content: @Composable () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = title,
            color = TextPrimary,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )

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
                    .padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                // Total principal grande
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = totalLabel,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = subtitleSize
                        )
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

                // Scroll horizontal con tarjetas
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    content()
                }
            }
        }
    }
}

/**
 * Modelo visual de equipos vistos.
 */
data class TeamStatUi(
    val name: String,
    val crestRes: Int,
    val matchesCount: String
)

/**
 * Modelo visual de jugadores.
 */
data class PlayerStatUi(
    val name: String,
    val crestRes: Int
)

/**
 * Modelo visual de eventos.
 */
data class EventStatUi(
    val name: String,
    val iconRes: Int,
    val total: String
)

/**
 * Modelo visual de estadios.
 */
data class StadiumStatUi(
    val name: String,
    val iconRes: Int
)

/**
 * Minicard para equipos con escudo y número de partidos.
 */
@Composable
private fun TeamMiniCard(
    item: TeamStatUi,
    cardWidth: androidx.compose.ui.unit.Dp,
    crestSize: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Image(
                painter = painterResource(id = item.crestRes),
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

            Text(
                text = item.name,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

/**
 * Minicard para jugadores con avatar genérico + escudo + nombre.
 */
@Composable
private fun PlayerMiniStatCard(
    item: PlayerStatUi,
    cardWidth: androidx.compose.ui.unit.Dp,
    avatarSize: androidx.compose.ui.unit.Dp,
    crestSize: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(avatarSize)
                    .clip(CircleShape)
                    .background(Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.profile_user),
                    contentDescription = "Jugador genérico",
                    modifier = Modifier.size(avatarSize * 0.55f),
                    contentScale = ContentScale.Fit
                )
            }

            Image(
                painter = painterResource(id = item.crestRes),
                contentDescription = item.name,
                modifier = Modifier.size(crestSize),
                contentScale = ContentScale.Fit
            )

            Text(
                text = item.name,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

/**
 * Minicard para eventos como amarillas o rojas.
 */
@Composable
private fun EventMiniCard(
    item: EventStatUi,
    cardWidth: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
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

            Text(
                text = item.total,
                color = PrimaryBlue,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Text(
                text = item.name,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

/**
 * Minicard para estadios vistos.
 */
@Composable
private fun StadiumMiniCard(
    item: StadiumStatUi,
    cardWidth: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(cardWidth),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = BackgroundLight
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

            Text(
                text = item.name,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}