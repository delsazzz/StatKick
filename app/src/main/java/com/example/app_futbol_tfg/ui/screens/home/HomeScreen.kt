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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.layout.ContentScale
import com.example.app_futbol_tfg.ui.components.ApiImage

@Composable
fun HomeScreen(userId: Int, db: AppDatabase, onNavigateBottom: (Int) -> Unit,onOpenTotalMatches: () -> Unit) {
    // Cargamos datos auxiliares para utilizar en la pantalla
    val partidosVistos by db.usuarioPartidoDao()
        .getPartidosByUsuario(userId)
        .collectAsState(initial = emptyList())
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val jugadores by db.jugadorDao().getAll().collectAsState(initial = emptyList())

    // Transformamos las listas en mapas clave-valor para que el acceso a los datos sea más eficiente y no recorra listas
    val equiposMap = equipos.associateBy { it.id }
    val jugadoresMap = jugadores.associateBy { it.id }

    // Generamos métricas básicas para las estadísticas
    val totalPartidos = partidosVistos.size
    val totalGoles = partidosVistos.sumOf { it.golesLocal + it.golesVisitante }
    val teamCounter = mutableMapOf<Int, Int>()

    partidosVistos.forEach { partido ->
        teamCounter[partido.idEquipoLocal] =
            (teamCounter[partido.idEquipoLocal] ?: 0) + 1
        teamCounter[partido.idEquipoVisitante] =
            (teamCounter[partido.idEquipoVisitante] ?: 0) + 1
    }

    val mostViewedTeam = teamCounter.maxByOrNull { it.value }?.key
    val mostViewedTeamName = mostViewedTeam?.let { equiposMap[it]?.nombre } ?: "-"
    val mostViewedTeamCrest = mostViewedTeam?.let { equiposMap[it]?.escudo }

    val matchIds = partidosVistos.map { it.id }

    val playerCounter = mutableMapOf<Int, Int>()

    val partidoJugadores by if (matchIds.isNotEmpty()) {
        db.partidoJugadorDao().getByPartidos(matchIds).collectAsState(initial = emptyList())
    } else {
        remember { mutableStateOf(emptyList()) }
    }

    partidoJugadores.forEach { pj ->
        playerCounter[pj.idJugador] =
            (playerCounter[pj.idJugador] ?: 0) + 1
    }

    val mostViewedPlayer = playerCounter.maxByOrNull { it.value }?.key

    val mostViewedPlayerName =
        mostViewedPlayer?.let { id ->
            val jugador = jugadoresMap[id]
            // joinToString ignora los nulls automáticamente
            // si apellido1 es null, solo muestra el nombre sin espacio raro
            listOfNotNull(jugador?.nombre, jugador?.apellido1)
                .joinToString(" ")
        } ?: "-"

    Scaffold(
        // Barra superior reutilizable
        topBar = {
            AppTopBar(
                title = "Mi perfil",
                showActionButton = true,
                actionIconRes = R.drawable.settings_gear,
                onActionClick = {
                    // Más adelante aquí podréis abrir ajustes
                }
            )
        },
        // Barra inferior reutilizable
        bottomBar = {
            AppBottomBar(
                selectedIndex = 0,
                onItemSelected = onNavigateBottom
            )
        },
        // Fondo principal de pantalla
        containerColor = BackgroundLight
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundLight)
        ) {
            // Ajustes adaptativos según tamaño disponible
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
                    .verticalScroll(rememberScrollState()) // Añadimos un scroll vertical
                    .padding(horizontal = horizontalPadding, vertical = 18.dp),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                // Primer bloque de la pantalla con la cabecera del usuario
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(cardCorner),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Foto genérica de perfil
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
                        // Nombre de usuario
                        Text(
                            text = "Sergio Álvarez",
                            color = TextPrimary,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = titleSize,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        // Texto secundario
                        Text(
                            text = "Usuario activo · Amante del fútbol europeo",
                            color = TextSecondary,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = subtitleSize
                            )
                        )
                    }
                }
                // Segundo bloque de la pantalla con estadísticas generales del usuario
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Resumen estadísticas",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    // Primera fila de estadísticas
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Partidos vistos",
                            value = "$totalPartidos",
                            onClick = onOpenTotalMatches
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Goles vistos",
                            value = "$totalGoles"
                        )
                    }
                    // Segunda fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Equipo más visto",
                            value = mostViewedTeamName,
                            imageUrl = mostViewedTeamCrest
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            title = "Jugador más visto",
                            value = mostViewedPlayerName,
                            imageUrl = mostViewedTeamCrest
                        )
                    }
                    // Si queremos añadir más estadísticas se pueden añadir más
                }
                // Tercer bloque de la pantalla con los logros del usuario
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Logros",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    AchievementCard(
                        title = "Primer partido registrado",
                        description = "Has añadido tu primer partido a la aplicación.",
                        iconRes = R.drawable.football_ball
                    )
                    AchievementCard(
                        title = "Fan del gol",
                        description = "Has superado los 300 goles visualizados.",
                        iconRes = R.drawable.football_goal
                    )
                    AchievementCard(
                        title = "Explorador de estadios",
                        description = "Ya tienes varios estadios guardados en el mapa.",
                        iconRes = R.drawable.map_pin
                    )
                }
                // Espacio final para que la bottom bar no quede pegada visualmente
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
// Card reutilizable para estadísticas rápidas del perfil.
// Recibe un título y un valor principal.
@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
    imageUrl: String? = null,
    onClick: (() -> Unit)? = null
) {
    Card(
        modifier = if (onClick != null) {
            modifier
                .wrapContentHeight()
                .clickable { onClick() }
        } else {
            modifier.wrapContentHeight()
        },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                color = TextSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
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
                   color = TextPrimary,
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
// Card reutilizable para los logros del usuario.
// Muestra icono + título + descripción.
@Composable
private fun AchievementCard(
    title: String,
    description: String,
    iconRes: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Caja circular para el icono
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
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = description,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}