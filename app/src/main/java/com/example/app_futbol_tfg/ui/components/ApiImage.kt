package com.example.app_futbol_tfg.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.AsyncImage
import com.example.app_futbol_tfg.R

// Componente reutilizable para cargar imágenes desde URL usando Coil.
// Si la URL es nula o falla la carga, muestra una imagen por defecto.
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
        // Imagen por defecto mientras carga o si hay error
        placeholder = painterResource(id = R.drawable.football_ball),
        error = painterResource(id = R.drawable.football_ball)
    )
}