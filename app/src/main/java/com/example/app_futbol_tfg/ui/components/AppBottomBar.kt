package com.example.app_futbol_tfg.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

// Clase que representa cada opción de la barra inferior
data class BottomNavItem(
    val label: String, // Texto debajo del icono
    val iconRes: Int, // Icono de la barra
)

@Composable
fun AppBottomBar(
    // Representa el índice del elemento seleccionado de la barra
    selectedIndex: Int,
    // Cuando pulsamos una opción se ejecuta una callback hacia esa opción
    onItemSelected: (Int) -> Unit
) {
    val items = listOf(
        BottomNavItem("Inicio", R.drawable.profile_user),
        BottomNavItem("Añadir", R.drawable.action_add),
        BottomNavItem("Stats", R.drawable.stats_chart),
        BottomNavItem("Mapa", R.drawable.map_pin)
    )

    NavigationBar(
        modifier = androidx.compose.ui.Modifier
            .background(CardBackground)
            .navigationBarsPadding(), // No se solapa con la barra inferior
        containerColor = CardBackground,
        tonalElevation = androidx.compose.ui.unit.Dp.Hairline
    ) {
        // Se recorre cada ítem para "pintarlo" en la barra
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                // Aquí se marca si el ítem es el que está activo
                selected = selectedIndex == index,
                // Acción que se realiza al pulsarlo
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                },
                // Texto del ítem
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                // colores de los ítems
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Color(0x142563EB)
                )
            )
        }
    }
}