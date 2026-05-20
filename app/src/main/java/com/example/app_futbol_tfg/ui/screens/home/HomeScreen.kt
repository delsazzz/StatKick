package com.example.app_futbol_tfg.ui.screens.home

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextAlign
import com.example.app_futbol_tfg.data.entity.UsuarioLogroEntity
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    userId: Int,
    db: AppDatabase,
    onNavigateBottom: (Int) -> Unit,
    onOpenTotalMatches: () -> Unit,
    onLogout: () -> Unit,
    onToggleDarkMode: () -> Unit,
    onEditProfile: () -> Unit,
    isDarkMode: Boolean
) {
    val appColors = LocalAppColors.current
    val backgroundColor = appColors.background
    val cardColor = appColors.card
    val textColorPrimary = appColors.textPrimary
    val textColorSecondary = appColors.textSecondary
    // Estados reactivos obtenidos desde Room
    // collectAsState permite actualizar automáticamente la UI cuando cambian los datos
    val partidosVistos by db.usuarioPartidoDao().getPartidosByUsuario(userId).collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val jugadores by db.jugadorDao().getAll().collectAsState(initial = emptyList())
    val usuario by db.usuarioDao().getByIdFlow(userId).collectAsState(initial = null)
    val usuarioLogrosState = db.usuarioLogroDao().getByUsuario(userId).collectAsState(initial = null)
    val usuarioLogros = usuarioLogrosState.value
    val unlockedAchievementIds = usuarioLogros?.map { it.idLogro }?.toSet() ?: emptySet()
    val equiposMap = equipos.associateBy { it.id }
    val jugadoresMap = jugadores.associateBy { it.id }
    // Cálculo de estadísticas generales a partir de los partidos registrados
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
    var achievementPopup by remember { mutableStateOf<AchievementUi?>(null) }
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
    val totalEquiposVistos = teamCounter.keys.size
    val totalJugadoresVistos = playerCounter.keys.size
    val totalEstadiosVistos = partidosVistos.mapNotNull { it.idEstadio }.distinct().size
    val totalCompeticionesVistas = partidosVistos.mapNotNull { it.idCompeticion }.distinct().size
    val totalVictoriasLocales = partidosVistos.count { it.golesLocal > it.golesVisitante }
    val totalEmpates = partidosVistos.count { it.golesLocal == it.golesVisitante }
    // Definición de logros desbloqueables según actividad del usuario
    val achievements = listOf(
        AchievementUi(
            id = 1,
            titulo = "Primer partido registrado",
            descripcion = "Has añadido tu primer partido a la aplicación.",
            iconRes = R.drawable.football_ball,
            desbloqueado = totalPartidos >= 1
        ),
        AchievementUi(
            id = 2,
            titulo = "Aficionado en marcha",
            descripcion = "Has registrado al menos 5 partidos.",
            iconRes = R.drawable.football_ball,
            desbloqueado = totalPartidos >= 5
        ),
        AchievementUi(
            id = 3,
            titulo = "Veterano de grada",
            descripcion = "Has registrado al menos 10 partidos.",
            iconRes = R.drawable.football_ball,
            desbloqueado = totalPartidos >= 10
        ),
        AchievementUi(
            id = 4,
            titulo = "Fan del gol",
            descripcion = "Has visto al menos 25 goles.",
            iconRes = R.drawable.football_goal,
            desbloqueado = totalGoles >= 25
        ),
        AchievementUi(
            id = 5,
            titulo = "Lluvia de goles",
            descripcion = "Has visto al menos 50 goles.",
            iconRes = R.drawable.football_goal,
            desbloqueado = totalGoles >= 50
        ),
        AchievementUi(
            id = 6,
            titulo = "Coleccionista de equipos",
            descripcion = "Has visto al menos 5 equipos diferentes.",
            iconRes = R.drawable.logo_escudo,
            desbloqueado = totalEquiposVistos >= 5
        ),
        AchievementUi(
            id = 7,
            titulo = "Plantilla conocida",
            descripcion = "Has visto al menos 10 jugadores diferentes.",
            iconRes = R.drawable.profile_user,
            desbloqueado = totalJugadoresVistos >= 10
        ),
        AchievementUi(
            id = 8,
            titulo = "Explorador de estadios",
            descripcion = "Has visitado al menos 3 estadios diferentes.",
            iconRes = R.drawable.map_pin,
            desbloqueado = totalEstadiosVistos >= 3
        ),
        AchievementUi(
            id = 9,
            titulo = "Ruta internacional",
            descripcion = "Has registrado partidos de varias competiciones.",
            iconRes = R.drawable.logo_trofeo,
            desbloqueado = totalCompeticionesVistas >= 2
        ),
        AchievementUi(
            id = 10,
            titulo = "Partido igualado",
            descripcion = "Has visto al menos un empate.",
            iconRes = R.drawable.football_ball,
            desbloqueado = totalEmpates >= 1
        )
    )
    // Detecta nuevos logros desbloqueados y los guarda en la BBDD
    // También muestra un pop-up temporal al usuario cuando consigue uno nuevo
    LaunchedEffect(achievements, usuarioLogros) {
        if (usuarioLogros == null) return@LaunchedEffect
        val nuevosLogros = achievements.filter { achievement ->
            achievement.desbloqueado && achievement.id !in unlockedAchievementIds
        }
        nuevosLogros.forEach { achievement ->
            val fechaActual = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).format(Date())
            db.usuarioLogroDao().insert(
                UsuarioLogroEntity(
                    idUsuario = userId,
                    idLogro = achievement.id,
                    fechaObtenido = fechaActual
                )
            )
            achievementPopup = achievement
            delay(3500)
            achievementPopup = null
            delay(300)
        }
    }
    // Estructura principal de la pantalla con top bar, contenido y navegación inferior
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
                            text = {
                                Text(
                                    text = if (isDarkMode) "Modo Claro" else "Modo oscuro") },
                            onClick = {
                                menuExpanded = false
                                onToggleDarkMode()
                            },
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.settings_gear),
                                    contentDescription =  if (isDarkMode) "Modo claro" else "Modo oscuro",
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
        // Adaptación responsive básica según el tamaño disponible de la pantalla (adaptativo)
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
                                painter = painterResource(id = getAvatarDrawable(usuario?.avatar)),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
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
                        Spacer(modifier = Modifier.height(14.dp))
                        TextButton(onClick = onEditProfile) {
                            Text(
                                text = "Editar perfil",
                                color = PrimaryBlue,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
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
                    achievements.forEach { achievement ->
                        AchievementCard(
                            title = achievement.titulo,
                            description = achievement.descripcion,
                            iconRes = achievement.iconRes,
                            unlocked = achievement.id in unlockedAchievementIds,
                            cardColor = cardColor,
                            textColorPrimary = textColorPrimary,
                            textColorSecondary = textColorSecondary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    achievementPopup?.let { achievement ->
        AlertDialog(
            onDismissRequest = { achievementPopup = null },
            title = {
                Text("¡Logro desbloqueado!")
            },
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        painter = painterResource(id = achievement.iconRes),
                        contentDescription = achievement.titulo,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = achievement.titulo,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = achievement.descripcion,
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { achievementPopup = null }) {
                    Text("Genial")
                }
            }
        )
    }
}
// Card reutilizable usada para mostrar las estadísticas resumidas
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
// Card visual utilizada para representar los logros
@Composable
private fun AchievementCard(
    title: String,
    description: String,
    iconRes: Int,
    unlocked: Boolean,
    cardColor: Color,
    textColorPrimary: Color,
    textColorSecondary: Color
) {
    val alpha = if (unlocked) 1f else 0.35f
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
                    tint = if (unlocked) PrimaryBlue else textColorSecondary,
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
                    color = textColorPrimary.copy(alpha = alpha),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Text(
                    text = if (unlocked) description else "Logro bloqueado",
                    color = textColorSecondary.copy(alpha = alpha),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}
data class AchievementUi (
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val iconRes: Int,
    val desbloqueado: Boolean
)
// Devuelve el recurso gráfico correspondiente al avatar seleccionado por el usuario
private fun getAvatarDrawable(avatar: String?): Int {
    return when (avatar) {
        "profile_user_1" -> R.drawable.avatar_cristiano_ronaldo
        "profile_user_2" -> R.drawable.avatar_messi
        "profile_user_3" -> R.drawable.avatar_uche
        else -> R.drawable.profile_user
    }
}