package com.example.app_futbol_tfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue

// Esta función se puede reutilizar para todas las pantallas 
// que incluyan una barra superior y se personaliza el título y botones 
// en función
@Composable
fun AppTopBar(
    title: String, // Título de cada pantalla
    showBackButton: Boolean = false, // Si se muestra el botón de volver
    showActionButton: Boolean = false, // Si se muestra un botón de acción a la derecha
    actionIconRes: Int? = null, 
    onBackClick: () -> Unit = {}, // Acción al pulsar el botón de volver
    onActionClick: () -> Unit = {} // Acción de pulsar el botón de la derecha
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            .safeDrawingPadding()
            .height(64.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Botón de volver
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.nav_back),
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }
        // Título principal de la barra
        Text(
            text = title,
            modifier = Modifier
                .weight(1f) // El título ocupa el espacio central
                .padding(start = if (showBackButton) 4.dp else 8.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        // Botón de acción de la derecha
        if (showActionButton && actionIconRes != null) {
            IconButton(onClick = onActionClick) {
                Icon(
                    painter = painterResource(id = actionIconRes),
                    contentDescription = "Acción",
                    tint = Color.White
                )
            }
        }
    }
}