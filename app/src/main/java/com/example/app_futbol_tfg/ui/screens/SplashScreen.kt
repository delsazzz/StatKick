package com.tfg.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tfg.app.R

@Composable
fun SplashScreen(
    modifier: Modifier = Modifier
) {
    val backgroundTop = Color(0xFF0F172A)   // azul marino oscuro
    val backgroundBottom = Color(0xFF111827)
    val accentBlue = Color(0xFF1F6FEB)
    val textColor = Color(0xFFFFFFFF)

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(backgroundTop, backgroundBottom)
                )
            )
            .safeDrawingPadding()
    ) {
        val isCompactHeight = maxHeight < 700.dp
        val logoSize = if (isCompactHeight) 150.dp else 210.dp
        val titleSize = if (isCompactHeight) 24.sp else 30.sp
        val subtitleSize = if (isCompactHeight) 12.sp else 14.sp
        val spacing = if (isCompactHeight) 18.dp else 26.dp

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // detalle visual suave de fondo
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(280.dp)
                    .alpha(0.10f)
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(accentBlue, Color.Transparent)
                        )
                    )
            )

            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo de la app",
                    modifier = Modifier.size(logoSize),
                    contentScale = ContentScale.Fit
                )

                Text(
                    text = "Goalytics",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = textColor,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = "Fútbol, datos y estadísticas",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = textColor.copy(alpha = 0.78f),
                        fontSize = subtitleSize
                    )
                )
            }

            Text(
                text = "TFG DAM",
                modifier = Modifier
                    .align(Alignment.BottomCenter),
                style = MaterialTheme.typography.bodySmall.copy(
                    color = textColor.copy(alpha = 0.45f),
                    fontSize = 11.sp
                )
            )
        }
    }
}