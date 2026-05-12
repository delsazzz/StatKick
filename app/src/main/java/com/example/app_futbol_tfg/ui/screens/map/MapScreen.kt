package com.example.app_futbol_tfg.ui.screens.map

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.components.AppBottomBar
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue

@Composable
fun MapScreen(onNavigateBottom: (Int) -> Unit) {
    val appColors = LocalAppColors.current

    Scaffold(
        topBar = {
            AppTopBar(title = "Mapa de estadios")
        },
        bottomBar = {
            AppBottomBar(
                selectedIndex = 3,
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
            val sectionSpacing = if (isSmallScreen) 16.dp else 22.dp
            val mapHeight = if (isSmallScreen) 320.dp else 420.dp
            val titleSize = if (isSmallScreen) 20.sp else 24.sp
            val pinSize = if (isSmallScreen) 24.dp else 30.dp

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = horizontalPadding, vertical = 18.dp)
                    .navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(sectionSpacing)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Estadios visitados",
                            color = appColors.textPrimary,
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontSize = titleSize,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Text(
                            text = "Esta pantalla representa visualmente el mapa de estadios. Más adelante aquí se integrará Google Maps y los marcadores reales con las coordenadas almacenadas en la base de datos.",
                            color = appColors.textSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth().height(mapHeight),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFEAF2FB))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(14.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color(0xFFDDEAF7))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.75f)
                                .height(6.dp)
                                .align(Alignment.Center)
                                .offset(y = (-40).dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.58f)
                                .height(6.dp)
                                .align(Alignment.Center)
                                .offset(y = 30.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                        )
                        Box(
                            modifier = Modifier
                                .width(6.dp)
                                .height(180.dp)
                                .align(Alignment.Center)
                                .offset(x = 50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.7f))
                        )
                        FakeMapPin(
                            modifier = Modifier.align(Alignment.TopStart).offset(x = 50.dp, y = 60.dp),
                            pinSize = pinSize, title = "Bernabéu"
                        )
                        FakeMapPin(
                            modifier = Modifier.align(Alignment.CenterStart).offset(x = 95.dp, y = 25.dp),
                            pinSize = pinSize, title = "Coliseum"
                        )
                        FakeMapPin(
                            modifier = Modifier.align(Alignment.BottomStart).offset(x = 130.dp, y = (-70).dp),
                            pinSize = pinSize, title = "Metropolitano"
                        )
                        FakeMapPin(
                            modifier = Modifier.align(Alignment.TopEnd).offset(x = (-70).dp, y = 90.dp),
                            pinSize = pinSize, title = "Camp Nou"
                        )
                        FakeMapPin(
                            modifier = Modifier.align(Alignment.BottomEnd).offset(x = (-90).dp, y = (-90).dp),
                            pinSize = pinSize, title = "Mestalla"
                        )
                        Text(
                            text = "Vista previa del mapa",
                            modifier = Modifier.align(Alignment.Center),
                            color = PrimaryBlue.copy(alpha = 0.6f),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    MapSummaryCard(modifier = Modifier.weight(1f), title = "Estadios", value = "11")
                    MapSummaryCard(modifier = Modifier.weight(1f), title = "Países", value = "4")
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = appColors.card),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Leyenda",
                            color = appColors.textPrimary,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        LegendItem(text = "Pin azul: estadio registrado en tu perfil")
                        LegendItem(text = "Más adelante aquí podrán mostrarse detalles al pulsar un estadio")
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun FakeMapPin(
    modifier: Modifier = Modifier,
    pinSize: androidx.compose.ui.unit.Dp,
    title: String
) {
    val appColors = LocalAppColors.current
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(pinSize + 10.dp)
                .clip(CircleShape)
                .background(PrimaryBlue.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.map_pin),
                contentDescription = title,
                tint = PrimaryBlue,
                modifier = Modifier.size(pinSize)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title,
            color = appColors.textPrimary,
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium)
        )
    }
}

@Composable
private fun MapSummaryCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    val appColors = LocalAppColors.current
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = appColors.card),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(text = title, color = appColors.textSecondary, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = value,
                color = PrimaryBlue,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}

@Composable
private fun LegendItem(text: String) {
    val appColors = LocalAppColors.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(PrimaryBlue)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text, color = appColors.textSecondary, style = MaterialTheme.typography.bodyMedium)
    }
}