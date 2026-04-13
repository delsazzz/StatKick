package com.example.app_futbol_tfg.ui.screens.matchdetail

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary
import com.example.app_futbol_tfg.ui.utils.getDrawableId
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton

@Composable
fun MatchDetailScreen(matchId: Int, userId: Int, db: AppDatabase, onBack: () -> Unit, onMatchAdded: () -> Unit) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var menuExpanded by remember { mutableStateOf(false) }
    var showRemoveDialog by remember { mutableStateOf(false) }

    // Se hace la llamada con el matchId y se guarda el resultado en partido
    val partidoState = produceState<com.example.app_futbol_tfg.data.entity.PartidoEntity?>(initialValue = null, matchId) {
        value = try {
            db.partidoDao().getById(matchId)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    val partido = partidoState.value
    // Con esto intentamos evitar que haya un crash cuando carga el partido
    if (partido == null) {
        Text("Cargando partido...")
        return
    }

    // Cargamos datos auxiliares para utilizar en la pantalla
    val equipos by db.equipoDao().getAll().collectAsState(initial = emptyList())
    val competiciones by db.competicionDao().getAll().collectAsState(initial = emptyList())
    val temporadas by db.temporadaDao().getAll().collectAsState(initial = emptyList())
    val estadios by db.estadioDao().getAll().collectAsState(initial = emptyList())
    val paises by db.paisDao().getAll().collectAsState(initial = emptyList())
    val localidades by db.localidadDao().getAll().collectAsState(initial = emptyList())

    // Transformamos las listas en mapas clave-valor para que el acceso a los datos sea más eficiente y no recorra listas
    val equiposMap = equipos.associateBy { it.id }
    val competicionesMap = competiciones.associateBy { it.id }
    val temporadasMap = temporadas.associateBy { it.id }
    val estadiosMap = estadios.associateBy { it.id }
    val paisesMap = paises.associateBy { it.id }
    val localidadesMap = localidades.associateBy { it.id }

    // Esto es el equivalente a un JOIN en SQL. Genera relaciones entre las entidades con los ids almacenados en Partidos
    val equipoLocal = partido.let { equiposMap[it.idEquipoLocal] }
    val equipoVisitante = partido.let { equiposMap[it.idEquipoVisitante] }
    val competicion = partido.idCompeticion?.let { competicionesMap[it] }
    val temporada = partido.idTemporada?.let { temporadasMap[it] }
    val estadio = partido.idEstadio?.let { estadiosMap[it] }
    val pais = competicion?.idPais?.let { paisesMap[it] }
    val pais_estadio = estadio?.idPais.let {paisesMap[it]}
    val localidad = estadio?.idLocalidad?.let { localidadesMap[it] }

    // Este paso nos va a decir si el partido ya está añadido o no
    val alreadyAddedState = produceState(initialValue = false, matchId, userId) {
        value = try {
            db.usuarioPartidoDao().getRelacion(userId, matchId) != null
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    var alreadyAdded by remember { mutableStateOf(false) }
    LaunchedEffect(alreadyAddedState.value) {
        alreadyAdded = alreadyAddedState.value
    }
    val scope = rememberCoroutineScope()
    // Cargamos los jugadores que han participado en los partidos desde Room
    val jugadoresPartido by db.jugadorDao().getDetalleByPartido(matchId).collectAsState(initial = emptyList())
    // Separamos a los jugadores por equipo local y visitante
    val jugadoresLocales = jugadoresPartido.filter { it.idEquipo == partido.idEquipoLocal }
    val jugadoresVisitantes = jugadoresPartido.filter { it.idEquipo == partido.idEquipoVisitante }
    // Generamos varuables para los escudos
    val localCrestRes = getDrawableId(context, equipoLocal?.escudo)
    val visitanteCrestRes = getDrawableId(context, equipoVisitante?.escudo)
    // Mapeamos a PlayerMatchUI los jugadores tanto locales como visitantes
    val localPlayersUi = jugadoresLocales.map { jugador ->
        PlayerMatchUi(
            nombre = jugador.nombre,
            apellido = getApellidoJugador(jugador.apellido1),
            crestRes = localCrestRes,
            titular = jugador.titular,
            minutos_jugados = jugador.minutos_jugados
        )
    }
    val visitantePlayersUi = jugadoresVisitantes.map { jugador ->
        PlayerMatchUi(
            nombre = jugador.nombre,
            apellido = getApellidoJugador(jugador.apellido1),
            crestRes = visitanteCrestRes,
            titular = jugador.titular,
            minutos_jugados = jugador.minutos_jugados
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            AppTopBar(
                title = "Detalle del partido",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        // Esta pantalla NO lleva barra inferior
        containerColor = BackgroundLight
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(BackgroundLight)
                .safeDrawingPadding()
        ) {
            // Ajustes adaptativos para distintos tamaños de pantalla
            val isSmallScreen = maxWidth < 360.dp || maxHeight < 700.dp
            val horizontalPadding = if (isSmallScreen) 14.dp else 20.dp
            val sectionSpacing = if (isSmallScreen) 16.dp else 22.dp
            val crestSize = if (isSmallScreen) 58.dp else 74.dp
            val scoreSize = if (isSmallScreen) 24.sp else 34.sp
            val teamNameSize = if (isSmallScreen) 14.sp else 16.sp
            val playerCardWidth = if (isSmallScreen) 96.dp else 108.dp
            val playerAvatarSize = if (isSmallScreen) 52.dp else 60.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                if (alreadyAdded) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box {
                            IconButton(
                                onClick = { menuExpanded = true }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "Más opciones",
                                    tint = TextPrimary
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Quitar de mi perfil") },
                                    onClick = {
                                        menuExpanded = false
                                        showRemoveDialog = true
                                    }
                                )
                            }
                        }
                    }
                }
                // Bloque principal con resultado, datos del partido
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = CardBackground
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Equipo local
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = getDrawableId(context, equipoLocal?.escudo)),
                                    contentDescription = equipoLocal?.nombre,
                                    modifier = Modifier.size(crestSize),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = equipoLocal?.nombre ?: "Local",
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = teamNameSize,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                            // Resultado
                            Text(
                                text = "${partido.golesLocal} - ${partido.golesVisitante}",
                                color = PrimaryBlue,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontSize = scoreSize,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            // Equipo visitante
                            Column(
                                modifier = Modifier.weight(1f),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = painterResource(id = getDrawableId(context, equipoVisitante?.escudo)),
                                    contentDescription = equipoVisitante?.nombre,
                                    modifier = Modifier.size(crestSize),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = equipoVisitante?.nombre ?: "Visitante",
                                    color = TextPrimary,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = teamNameSize,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                )
                            }
                        }
                    }
                }
                // Bloque con más información del partido
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
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = partido.fecha,
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DotSeparator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = temporada?.temporada ?: "Temporada",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DotSeparator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = competicion?.nombre ?: "Competición",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(id = getDrawableId(context, pais?.bandera)),
                                contentDescription = pais?.nombre,
                                modifier = Modifier.size(18.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.logo_estadio),
                                contentDescription = "Logo de estadio",
                                modifier = Modifier.size(18.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = estadio?.nombre ?: "Estadio",
                                color = TextPrimary,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                                )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DotSeparator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${localidad?.nombre} (${pais_estadio?.nombre})",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
                // Bloque con jugadores del partido
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Jugadores participantes",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = equipoLocal?.nombre ?: "Equipo local",
                        color = TextSecondary,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    // En esta fila añadimos los jugadores del equipo local
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Añadimos un scroll horizontal para poder ver todos los jugadores
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        localPlayersUi.forEach { player ->
                            PlayerMiniCard(
                                player = player,
                                width = playerCardWidth,
                                avatarSize = playerAvatarSize
                            )
                        }
                    }
                    Text(
                        text = equipoVisitante?.nombre ?: "Equipo visitante",
                        color = TextSecondary,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    // En esta fila añadimos los jugadores del equipo visitante
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Añadimos un scroll horizontal para poder ver todos los jugadores
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        visitantePlayersUi.forEach { player ->
                            PlayerMiniCard(
                                player = player,
                                width = playerCardWidth,
                                avatarSize = playerAvatarSize
                            )
                        }
                    }
                }
                // Botón para añadir los partidos a nuestro perfil
                Button(
                    onClick = {
                        scope.launch {
                            try {
                                val fechaActual = java.text.SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    java.util.Locale.getDefault()
                                ).format(java.util.Date())
                                db.usuarioPartidoDao().insert(
                                    com.example.app_futbol_tfg.data.entity.UsuarioPartidoEntity(
                                        idUsuario = userId,
                                        idPartido = matchId,
                                        fechaRegistro = fechaActual
                                    )
                                )
                                alreadyAdded = true
                                snackbarHostState.showSnackbar("Partido añadido correctamente")
                                onMatchAdded() // vuelve a Home
                            } catch (e: Exception) {
                                e.printStackTrace()
                                snackbarHostState.showSnackbar("No se pudo añadir el partido")
                            }
                        }
                    },
                    enabled = !alreadyAdded,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrimaryBlue,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.action_add),
                        contentDescription = "Añadir partido",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if(alreadyAdded)
                            "Partido ya añadido"
                            else
                            "Añadir partido a mi perfil",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
    if (showRemoveDialog) {
        AlertDialog(
            onDismissRequest = { showRemoveDialog = false },
            title = {
                Text("Quitar partido")
            },
            text = {
                Text("Eliminarás el partido de tu perfil. ¿Quieres continuar?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showRemoveDialog = false
                        scope.launch {
                            try {
                                db.usuarioPartidoDao().deleteRelacion(userId, matchId)
                                alreadyAdded = false
                                snackbarHostState.showSnackbar("Partido eliminado de tu perfil")
                                onBack()
                            } catch (e: Exception) {
                                e.printStackTrace()
                                snackbarHostState.showSnackbar("No se pudo eliminar el partido")
                            }
                        }
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showRemoveDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// Modelo para los jugadores
data class PlayerMatchUi(
    val nombre: String,
    val apellido: String,
    val crestRes: Int,
    val titular: Boolean,
    val minutos_jugados: Int?
)
// Modelo para los jugadores
data class JugadorPartidoDetalle(
    val id: Int,
    val nombre: String,
    val apellido1: String?,
    val apellido2: String? = null,
    val idEquipo: Int,
    val titular: Boolean,
    val minutos_jugados: Int?
)

// Minicard horizontal de jugador que muestra el avatar genérico, escudo, nombre
@Composable
private fun PlayerMiniCard(
    player: PlayerMatchUi,
    width: androidx.compose.ui.unit.Dp,
    avatarSize: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier
            .width(120.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // Avatar genérico del jugador
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
                // Escudo del equipo
                Image(
                    painter = painterResource(id = player.crestRes),
                    contentDescription = "Escudo equipo jugador",
                    modifier = Modifier.size(22.dp),
                    contentScale = ContentScale.Fit
                )
                // Nombre y apellido del jugador
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = player.nombre,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                    Text(
                        text = player.apellido,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        color = TextPrimary,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (player.titular) Color(0xFF22C55E) else Color(0xFFEF4444)
                    )
                    .padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
                Text(
                    text = "${player.minutos_jugados ?: 0}'",
                    color = Color.White,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        }
    }
}

private fun getApellidoJugador(
    apellido1: String?
): String {
    return listOfNotNull(apellido1).joinToString(" ")
}
@Composable
private fun DotSeparator() {
    Text(
        text = "•",
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )
}