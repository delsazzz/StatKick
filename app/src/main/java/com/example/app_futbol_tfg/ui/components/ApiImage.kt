package com.example.app_futbol_tfg.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.app_futbol_tfg.R

// Componente reutilizable para cargar imágenes remotas utilizando Coil
// En caso de error o ausencia de imagen se muestra un recurso por defecto
@Composable
fun ApiImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit
) {
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        // Imagen placeholder utilizada durante la carga o ante errores
        placeholder = painterResource(id = R.drawable.football_ball),
        error = painterResource(id = R.drawable.football_ball)
    )
}