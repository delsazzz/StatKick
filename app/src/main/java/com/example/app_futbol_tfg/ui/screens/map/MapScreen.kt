package com.example.app_futbol_tfg.ui.screens.map

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

import com.example.app_futbol_tfg.data.database.DatabaseProvider
import com.example.app_futbol_tfg.data.entity.EstadioEntity
import com.example.app_futbol_tfg.data.repository.EstadioRepository
import com.example.app_futbol_tfg.data.repository.UsuarioPartidoRepository

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun MapScreen(onNavigateBottom: (Int) -> Unit) {

    val context = LocalContext.current
    val scope   = rememberCoroutineScope()
    val db      = remember { DatabaseProvider.getDatabase(context) }

    // ── Repositorios ───────────────────────────────────────────────────────
    val estadioRepo        = remember { EstadioRepository(db.estadioDao()) }
    val usuarioPartidoRepo = remember { UsuarioPartidoRepository(db.usuarioPartidoDao()) }

    // ── ID del usuario logueado ────────────────────────────────────────────
    // TODO: sustituir por el id real del usuario logueado cuando esté implementado
    // Debe coincidir con DEMO_USER_ID de MainActivity (actualmente = 2)
    val idUsuario = 2

    // ── Partidos vistos por el usuario ─────────────────────────────────────
    val partidosVistos by usuarioPartidoRepo
        .getPartidosByUsuario(idUsuario)
        .collectAsState(initial = emptyList())

    // ── Estadios donde el usuario ha visto partidos ────────────────────────
    val todosEstadios by estadioRepo.getAll().collectAsState(initial = emptyList())

    val idsEstadiosVistos = remember(partidosVistos) {
        partidosVistos.mapNotNull { it.idEstadio }.toSet()
    }

    val estadiosVistos = remember(todosEstadios, idsEstadiosVistos) {
        todosEstadios.filter { it.id in idsEstadiosVistos }
    }

    // ── Cámara centrada en Europa ──────────────────────────────────────────
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(48.0, 8.0), 4f)
    }

    // ── Estadio seleccionado al pulsar marcador ────────────────────────────
    var estadioSeleccionado by remember { mutableStateOf<EstadioEntity?>(null) }

    // ── Partidos vistos por el usuario en el estadio seleccionado ─────────
    // Usamos countPartidosEnEstadio que cruza Usuario_Partido con Partidos
    // para contar SOLO los partidos del usuario, no todos los del estadio
    val partidosEnEstadio by remember(estadioSeleccionado) {
        estadioSeleccionado?.let {
            usuarioPartidoRepo.countPartidosEnEstadio(idUsuario, it.id)
        } ?: flowOf(0)
    }.collectAsState(initial = 0)

    val mapProperties = remember { MapProperties(mapType = MapType.HYBRID) }
    val mapUiSettings = remember {
        MapUiSettings(
            zoomControlsEnabled     = true,
            compassEnabled          = true,
            myLocationButtonEnabled = false
        )
    }

    Scaffold(
        topBar    = { AppTopBar(title = "Mis estadios") },
        bottomBar = { AppBottomBar(selectedIndex = 3, onItemSelected = onNavigateBottom) }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            GoogleMap(
                modifier            = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                properties          = mapProperties,
                uiSettings          = mapUiSettings
            ) {
                estadiosVistos.forEach { estadio ->
                    val coords = if (estadio.latitud != null && estadio.longitud != null)
                        LatLng(estadio.latitud, estadio.longitud)
                    else null

                    if (coords != null) {
                        Marker(
                            state   = MarkerState(position = coords),
                            title   = estadio.nombre,
                            icon    = BitmapDescriptorFactory.defaultMarker(
                                BitmapDescriptorFactory.HUE_GREEN
                            ),
                            onClick = { _ ->
                                estadioSeleccionado = estadio
                                scope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(coords, 15f),
                                        durationMs = 1000
                                    )
                                }
                                true
                            }
                        )
                    }
                }
            }

            // ── Mensaje si el usuario no ha añadido ningún partido todavía ─
            if (estadiosVistos.isEmpty()) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp)
                        .fillMaxWidth(),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    shape     = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text       = "Aún no has visto ningún partido",
                            fontWeight = FontWeight.Bold,
                            color      = TextPrimary
                        )
                        Text(
                            text     = "Añade partidos para ver los estadios que has visitado en el mapa.",
                            fontSize = 13.sp,
                            color    = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            // ── Contador de estadios visitados ─────────────────────────────
            if (estadiosVistos.isNotEmpty()) {
                Card(
                    modifier  = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    colors    = CardDefaults.cardColors(containerColor = Color.White),
                    shape     = RoundedCornerShape(20.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Text(
                        text       = "🏟️ ${estadiosVistos.size} ${if (estadiosVistos.size == 1) "estadio visitado" else "estadios visitados"}",
                        modifier   = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color      = PrimaryBlue
                    )
                }
            }

            // ── Card flotante con info del estadio seleccionado ────────────
            AnimatedVisibility(
                visible  = estadioSeleccionado != null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit  = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                estadioSeleccionado?.let { estadio ->
                    TarjetaEstadio(
                        estadio  = estadio,
                        partidos = partidosEnEstadio,
                        onCerrar = {
                            estadioSeleccionado = null
                            scope.launch {
                                cameraPositionState.animate(
                                    CameraUpdateFactory.newLatLngZoom(LatLng(48.0, 8.0), 4f)
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

// ─── Card con info del estadio ────────────────────────────────────────────────
@Composable
private fun TarjetaEstadio(
    estadio:  EstadioEntity,
    partidos: Int,
    onCerrar: () -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors    = CardDefaults.cardColors(containerColor = Color.White),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Nombre + botón cerrar
            Row(
                modifier              = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text(
                    text       = estadio.nombre,
                    style      = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color      = TextPrimary,
                    modifier   = Modifier.weight(1f)
                )
                IconButton(onClick = onCerrar) {
                    Icon(
                        imageVector        = Icons.Default.Close,
                        contentDescription = "Cerrar",
                        tint               = TextSecondary
                    )
                }
            }

            // Capacidad
            estadio.capacidad?.let {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("🏟️", fontSize = 14.sp)
                    Text(
                        text  = "$it espectadores",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }

            // Partidos vistos por el usuario en este estadio
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment     = Alignment.CenterVertically
            ) {
                Text("⚽", fontSize = 14.sp)
                Text(
                    text       = if (partidos == 1) "1 partido visto aquí"
                    else "$partidos partidos vistos aquí",
                    style      = MaterialTheme.typography.bodyMedium,
                    color      = PrimaryBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }

            // Dirección
            if (!estadio.direccion.isNullOrBlank()) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text("📍", fontSize = 14.sp)
                    Text(
                        text  = estadio.direccion,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}