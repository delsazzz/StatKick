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
import androidx.compose.foundation.layout.fillMaxHeight
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
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

@Composable
fun MatchDetailScreen() {
    // Lista mock visual de jugadores.
    // Más adelante vendrá del partido seleccionado en la base de datos.
    val players = listOf(
        PlayerMatchUi("David Soria", R.drawable.escudo_getafe),
        PlayerMatchUi("Djené", R.drawable.escudo_getafe),
        PlayerMatchUi("Mason Greenwood", R.drawable.escudo_getafe),
        PlayerMatchUi("Borja Mayoral", R.drawable.escudo_getafe),
        PlayerMatchUi("Óscar Rodríguez", R.drawable.escudo_psg),
        PlayerMatchUi("Neyou", R.drawable.escudo_psg),
        PlayerMatchUi("Miguel de la Fuente", R.drawable.escudo_psg),
        PlayerMatchUi("Sergio González", R.drawable.escudo_psg)
    )
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Detalle del partido",
                showBackButton = true,
                onBackClick = {
                    // Más adelante: volver atrás
                }
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
                    .padding(horizontal = horizontalPadding, vertical = 18.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
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
                                    painter = painterResource(id = R.drawable.escudo_getafe),
                                    contentDescription = "Getafe",
                                    modifier = Modifier.size(crestSize),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Getafe",
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
                                text = "2 - 1",
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
                                    painter = painterResource(id = R.drawable.escudo_psg),
                                    contentDescription = "Leganés",
                                    modifier = Modifier.size(crestSize),
                                    contentScale = ContentScale.Fit
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Leganés",
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
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "12/03/2024",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DotSeparator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "2023/24",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            DotSeparator()
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "LaLiga EA Sports",
                                color = TextSecondary,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Image(
                                painter = painterResource(id = R.drawable.bandera_espana),
                                contentDescription = "Bandera de la competición",
                                modifier = Modifier.size(18.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                        Text(
                            text = "Coliseum",
                            color = TextPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
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
                    // En esta fila añadimos los jugadores del equipo local
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Añadimos un scroll horizontal para poder ver todos los jugadores
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        players.forEach { player ->
                            PlayerMiniCard(
                                player = player,
                                width = playerCardWidth,
                                avatarSize = playerAvatarSize
                            )
                        }
                    }
                    // En esta fila añadimos los jugadores del equipo visitante
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // Añadimos un scroll horizontal para poder ver todos los jugadores
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        players.forEach { player ->
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
                        // Más adelante:
                        // 1. añadir partido al perfil del usuario
                        // 2. actualizar estadísticas
                    },
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
                        text = "Añadir partido a mi perfil",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}
// Modelo para los jugadores
data class PlayerMatchUi(
    val name: String,
    val crestRes: Int
)
// Minicard horizontal de jugador que muestra el avatar genérico, escudo, nombre
@Composable
private fun PlayerMiniCard(
    player: PlayerMatchUi,
    width: androidx.compose.ui.unit.Dp,
    avatarSize: androidx.compose.ui.unit.Dp
) {
    Card(
        modifier = Modifier.width(width),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
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
            // Nombre del jugador
            Text(
                text = player.name,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}
@Composable
private fun DotSeparator() {
    Text(
        text = "•",
        color = TextSecondary,
        style = MaterialTheme.typography.bodySmall
    )
}