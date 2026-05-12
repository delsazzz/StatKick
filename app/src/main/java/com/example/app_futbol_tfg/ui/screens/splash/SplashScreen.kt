package com.example.app_futbol_tfg.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.BlueGrey
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryDark

@Composable
fun SplashScreen() {
    // BoxWithConstraints sirve para controlar la adaptabilidad de la pantalla
    // Aprovecha el espacio disponible y se adapta según el móvil
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                // Aplicamos un degradado al fondo
                brush = Brush.verticalGradient(
                    colors = listOf(
                        PrimaryBlue, // Parte superior
                        BlueGrey // Parte inferior
                    )
                )
            )
            // Evitamos que los elementos interactivos se superpongan
            .safeDrawingPadding() 
    ) {

        // Si la pantalla tiene poca altura, se reduce el tamaño
        val isSmallScreen = maxHeight < 700.dp

        // Tamaño adaptable del logo
        val logoSize = if (isSmallScreen) 200.dp else 260.dp

        // Tamaños adaptables de texto
        val titleSize = if (isSmallScreen) 24.sp else 30.sp
        val subtitleSize = if (isSmallScreen) 12.sp else 14.sp

        // Separación adaptable entre logo y textos
        val spacing = if (isSmallScreen) 18.dp else 26.dp

        Box(
            modifier = Modifier.fillMaxSize()
        ) {

            // Halo visual decorativo en la parte superior
            // No es obligatorio, pero da un acabado más "premium"
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(280.dp)
                    .alpha(0.10f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                PrimaryBlue,
                                Color.Transparent
                            )
                        )
                    )
            )

            // Columna central donde colocamos logo + nombre app + subtítulo
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {

                // LOGO DE LA APP
                // Sustituye "logo_app" por el nombre real de tu logo en drawable si hace falta
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo de la aplicación",
                    modifier = Modifier.size(logoSize),
                    contentScale = ContentScale.Fit
                )

                // NOMBRE DE LA APP
                Text(
                    text = "StatKick",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold
                    )
                )

                // SUBTÍTULO
                Text(
                    text = "Fútbol, datos y estadísticas",
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = subtitleSize
                    )
                )
            }
        }
    }
}