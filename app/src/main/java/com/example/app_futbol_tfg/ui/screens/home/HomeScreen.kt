package com.example.app_futbol_tfg.ui.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.ApiImage
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue

@Composable
fun HomeScreen(
    userId: Int,
    db: AppDatabase,
    onNavigateBottom: (Int) -> Unit,
    onOpenTotalMatches: () -> Unit,
    onLogout: () -> Unit,
    onToggleDarkMode: () -> Unit
) {
    val appColors = LocalAppColors.current
    val backgroundColor = appColors.background
    val cardColor = appColors.card
    val textColorPrimary = appColors.textPrimary
    val textColorSecondary = appColors.textSecondary

    val partidosVistos by db.usuarioPartidoDao()
        .getPartidosByUsuario(userId)
        .collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val jugadores by db.jugadorDao().getAll().collectAsState(initial = emptyList())
    val usuario by db.usuarioDao().getByIdFlow(userId).collectAsState(initial = null)

    val equiposMap = equipos.associateBy { it.id }
    val jugadoresMap = jugadores.associateBy { it.id }

    val totalPartidos = partidosVistos.size
    val totalGoles = partidosVistos.sumOf { it.golesLocal + it.golesVisitante }
    val teamCounter = mutableMapOf<Int, Int>()

    partidosVistos.forEach { partido ->
        teamCounter[partido.idEquipoLocal] = (teamCounter[partido.idEquipoLocal] ?: 0) + 1
        teamCounter[partido.idEquipoVisitante] = (teamCounter[partido.idEquipoVisitante] ?: 0) + 1
    }

    val mostViewedTeam = teamCounter.maxByOrNull { it.value }?.key
    val mostViewedTeamName = mostViewedTeam?.let { equiposMap[it]?.nombre } ?: "-"
    val mostViewedTeamCrest = mostViewedTeam?.let { equiposMap[it]?.escudo }

    val matchIds = partidosVistos.map { it.id }
    val playerCounter = mutableMapOf<Int, Int>()
    var menuExpanded by remember { mutableStateOf(false) }

    val partidoJugadores by if (matchIds.isNotEmpty()) {
        db.partidoJugadorDao().getByPartidos(matchIds).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    partidoJugadores.forEach { pj ->
        playerCounter[pj.idJugador] = (playerCounter[pj.idJugador] ?: 0) + 1
    }

    val mostViewedPlayer = playerCounter.maxByOrNull { it.value }?.key
    val mostViewedPlayerName = mostViewedPlayer?.let { id ->
        val jugador = jugadoresMap[id]
        listOfNotNull(jugador?.nombre, jugador?.apellido1).joinToString(" ")
    } ?: "-"

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Mi perfil",
                showActionButton = true,
                actionIconRes = R.drawable.settings_gear,
                onActionClick = { menuExpanded = true },
                dropdownContent = {
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Modo oscuro") },
                            onClick = {
                                menuExpanded = false
                                onToggleDarkMode()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.settings_gear),
                                    contentDescription = "Modo oscuro",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                menuExpanded = false
                                onLogout()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.settings_gear),
                                    contentDescription = "Cerrar sesión",
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        )
                    }
                }
            )
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 0,
                onItemSelected = onNavigateBottom
            )
        },
        containerColor = backgroundColor
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(backgroundColor)
        ) {
            val isSmallScreen = maxWidth < 360.dp || maxHeight < 700.dp
            val avatarSize = if (isSmallScreen) 84.dp else 108.dp
            val titleSize = if (isSmallScreen) 22.sp else 28.sp
            val subtitleSize = if (isSmallScreen) 13.sp else 15.sp
            val horizontalPadding = if (isSmallScreen) 16.dp else 20.dp
            val sectionSpacing = if (isSmallScreen) 16.dp else 22.dp
            val cardCorner = 22.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(cardCorner),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
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
                                contentDescription = "Foto de perfil genérica",
                                modifier = Modifier.size(avatarSize * 0.58f)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = usuario?.nombreUsuario ?: "Usuario",
                            color = textColorPrimary,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = titleSize,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Usuario activo · ${usuario?.email ?: ""}",
                            color = textColorSecondary,
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = subtitleSize)
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Resumen estadísticas",
                        color = textColorPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Partidos vistos",
                            value = "$totalPartidos",
                            onClick = onOpenTotalMatches,
                            cardColor = cardColor,
                            textColorPrimary = textColorPrimary,
                            textColorSecondary = textColorSecondary
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Goles vistos",
                            value = "$totalGoles",
                            cardColor = cardColor,
                            textColorPrimary = textColorPrimary,
                            textColorSecondary = textColorSecondary
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Equipo más visto",
                            value = mostViewedTeamName,
                            imageUrl = mostViewedTeamCrest,
                            cardColor = cardColor,
                            textColorPrimary = textColorPrimary,
                            textColorSecondary = textColorSecondary
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Jugador más visto",
                            value = mostViewedPlayerName,
                            cardColor = cardColor,
                            textColorPrimary = textColorPrimary,
                            textColorSecondary = textColorSecondary
                        )
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Logros",
                        color = textColorPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                    )
                    AchievementCard(
                        title = "Primer partido registrado",
                        description = "Has añadido tu primer partido a la aplicación.",
                        iconRes = R.drawable.football_ball,
                        cardColor = cardColor,
                        textColorPrimary = textColorPrimary,
                        textColorSecondary = textColorSecondary
                    )
                    AchievementCard(
                        title = "Fan del gol",
                        description = "Has superado los 300 goles visualizados.",
                        iconRes = R.drawable.football_goal,
                        cardColor = cardColor,
                        textColorPrimary = textColorPrimary,
                        textColorSecondary = textColorSecondary
                    )
                    AchievementCard(
                        title = "Explorador de estadios",
                        description = "Ya tienes varios estadios guardados en el mapa.",
                        iconRes = R.drawable.map_pin,
                        cardColor = cardColor,
                        textColorPrimary = textColorPrimary,
                        textColorSecondary = textColorSecondary
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    imageUrl: String? = null,
    onClick: (() -> Unit)? = null,
    cardColor: Color,
    textColorPrimary: Color,
    textColorSecondary: Color
) {
    Card(
        modifier = if (onClick != null) modifier.wrapContentHeight().clickable { onClick() }
        else modifier.wrapContentHeight(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = title, color = textColorSecondary, style = MaterialTheme.typography.bodyMedium)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                if (imageUrl != null) {
                    ApiImage(
                        url = imageUrl,
                        contentDescription = value,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Text(
                    text = value,
                    color = textColorPrimary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    softWrap = true,
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )
            }
        }
    }
}

@Composable
private fun AchievementCard(
    title: String,
    description: String,
    iconRes: Int,
    cardColor: Color,
    textColorPrimary: Color,
    textColorSecondary: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(PrimaryBlue.copy(alpha = 0.10f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = title,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    color = textColorPrimary,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = description,
                    color = textColorSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}