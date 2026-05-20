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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue

// Modelo utilizado para representar cada elemento de navegación inferior
data class BottomNavItem(
    val label: String,
    val iconRes: Int,
)
// Barra de navegación inferior reutilizable utilizada en las pantallas principales
@Composable
fun AppBottomBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val appColors = LocalAppColors.current
    // Elementos disponibles dentro de la navegación principal
    val items = listOf(
        BottomNavItem("Inicio", R.drawable.logo_centro_campo),
        BottomNavItem("Añadir", R.drawable.action_add),
        BottomNavItem("Stats", R.drawable.stats_chart),
        BottomNavItem("Mapa", R.drawable.map_pin)
    )
    NavigationBar(
        modifier = Modifier
            .background(appColors.card)
            .navigationBarsPadding(),
        containerColor = appColors.card,
        tonalElevation = Dp.Hairline
    ) {
        // Generación dinámica de los elementos de navegación
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = {
                    Icon(
                        painter = painterResource(id = item.iconRes),
                        contentDescription = item.label
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = appColors.textSecondary,
                    unselectedTextColor = appColors.textSecondary,
                    indicatorColor = Color(0x142563EB)
                )
            )
        }
    }
}