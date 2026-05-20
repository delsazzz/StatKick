package com.example.app_futbol_tfg.ui.screens.editprofile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.components.AppTopBar
import com.example.app_futbol_tfg.ui.components.CustomTextField
import com.example.app_futbol_tfg.ui.components.PrimaryButton
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.horizontalScroll

@Composable
fun EditProfileScreen(
    userId: Int,
    db: AppDatabase,
    onBack: () -> Unit
) {
    val appColors = LocalAppColors.current
    // Estados y corrutinas utilizados para gestionar la edición del perfil
    val scope = rememberCoroutineScope()
    val usuario by db.usuarioDao().getByIdFlow(userId).collectAsState(initial = null)
    var nombreUsuario by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("profile_user") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    // Sincroniza los datos actuales del usuario con los estados locales de la pantalla
    LaunchedEffect(usuario) {
        usuario?.let {
            nombreUsuario = it.nombreUsuario
            selectedAvatar = it.avatar
        }
    }
    // Avatares disponibles que el usuario puede seleccionar
    val avatarOptions = listOf(
        "profile_user" to R.drawable.profile_user,
        "profile_user_1" to R.drawable.avatar_cristiano_ronaldo,
        "profile_user_2" to R.drawable.avatar_messi,
        "profile_user_3" to R.drawable.avatar_uche
    )
    // Estructura principal de la pantalla de edición de perfil
    Scaffold(
        topBar = {
            AppTopBar(
                title = "Editar perfil",
                showBackButton = true,
                onBackClick = onBack
            )
        },
        containerColor = appColors.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(appColors.background)
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = appColors.card),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Elige tu avatar",
                        color = appColors.textPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Selector horizontal de avatares disponibles
                        avatarOptions.forEach { avatar ->
                            val isSelected = selectedAvatar == avatar.first
                            Box(
                                modifier = Modifier
                                    .size(if (isSelected) 82.dp else 70.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) PrimaryBlue.copy(alpha = 0.20f)
                                        else Color(0xFFE2E8F0)
                                    )
                                    .clickable {
                                        selectedAvatar = avatar.first
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    painter = painterResource(id = avatar.second),
                                    contentDescription = avatar.first,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                    CustomTextField(
                        value = nombreUsuario,
                        onValueChange = {
                            nombreUsuario = it
                            errorMessage = null
                        },
                        label = "Nombre de usuario",
                        isError = errorMessage != null
                    )
                    errorMessage?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    // Validación y actualización de los datos del perfil del usuario
                    PrimaryButton(
                        text = "Guardar cambios",
                        isLoading = isLoading,
                        onClick = {
                            scope.launch {
                                val nuevoNombre = nombreUsuario.trim()
                                if (nuevoNombre.isBlank()) {
                                    errorMessage = "El nombre de usuario no puede estar vacío"
                                    return@launch
                                }
                                isLoading = true
                                val existe = db.usuarioDao()
                                    .existeNombreUsuarioEnOtroUsuario(nuevoNombre, userId)
                                if (existe) {
                                    errorMessage = "Ese nombre de usuario ya está en uso"
                                    isLoading = false
                                    return@launch
                                }
                                db.usuarioDao().actualizarPerfil(
                                    userId = userId,
                                    nuevoNombre = nuevoNombre,
                                    nuevoAvatar = selectedAvatar
                                )
                                isLoading = false
                                onBack()
                            }
                        }
                    )
                }
            }
        }
    }
}