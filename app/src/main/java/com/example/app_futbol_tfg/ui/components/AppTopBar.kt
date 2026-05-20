package com.example.app_futbol_tfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.foundation.layout.statusBarsPadding

// Componente reutilizable utilizado como barra superior común de la aplicación
@Composable
fun AppTopBar(
    title: String,
    showBackButton: Boolean = false,
    showActionButton: Boolean = false,
    actionIconRes: Int? = null,
    onBackClick: () -> Unit = {},
    onActionClick: () -> Unit = {},
    dropdownContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (showBackButton) {
            IconButton(onClick = onBackClick) {
                Icon(
                    painter = painterResource(id = R.drawable.nav_back),
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        }
        Text(
            text = title,
            modifier = Modifier
                .weight(1f)
                .padding(start = if (showBackButton) 4.dp else 8.dp),
            color = Color.White,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.SemiBold
            )
        )
        // Contenedor del botón de acción y sus elementos asociados.
        if (showActionButton && actionIconRes != null) {
            Box {
                IconButton(onClick = onActionClick) {
                    Icon(
                        painter = painterResource(id = actionIconRes),
                        contentDescription = "Acción",
                        tint = Color.White
                    )
                }
                dropdownContent()
            }
        }
    }
}