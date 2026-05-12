package com.example.app_futbol_tfg.ui.screens.splash

import androidx.compose.foundation.Image
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.BlueGrey
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    isLoggedIn: Boolean,
    onNavigateToHome: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    // Al arrancar esperamos 2 segundos y navegamos según el estado de sesión
    LaunchedEffect(Unit) {
        delay(2000)
        if (isLoggedIn) {
            onNavigateToHome()
        } else {
            onNavigateToLogin()
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(PrimaryBlue, BlueGrey)
                )
            )
            .safeDrawingPadding()
    ) {
        val isSmallScreen = maxHeight < 700.dp
        val logoSize = if (isSmallScreen) 200.dp else 260.dp
        val titleSize = if (isSmallScreen) 24.sp else 30.sp
        val subtitleSize = if (isSmallScreen) 12.sp else 14.sp
        val spacing = if (isSmallScreen) 18.dp else 26.dp

        Box(modifier = Modifier.fillMaxSize()) {

            // Halo decorativo superior
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(280.dp)
                    .alpha(0.10f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(PrimaryBlue, Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo de la aplicación",
                    modifier = Modifier.size(logoSize),
                    contentScale = ContentScale.Fit
                )
                Text(
                    text = "StatKick",
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold
                    )
                )
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