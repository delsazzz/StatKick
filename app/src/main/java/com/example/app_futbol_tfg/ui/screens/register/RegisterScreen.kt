package com.example.app_futbol_tfg.ui.screens.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.app_futbol_tfg.ui.components.CustomTextField
import com.example.app_futbol_tfg.ui.components.PasswordTextField
import com.example.app_futbol_tfg.ui.components.PrimaryButton
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.viewmodels.RegisterUiState
import com.example.app_futbol_tfg.ui.viewmodels.RegisterViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegisterSuccess: (Int) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val appColors = LocalAppColors.current
    var nombreUsuario by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {
        if (uiState is RegisterUiState.Success) {
            onRegisterSuccess((uiState as RegisterUiState.Success).userId)
            viewModel.resetState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo de StatKick",
            modifier = Modifier.size(170.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "StatKick",
            color = PrimaryBlue,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Crea tu cuenta y empieza a registrar partidos",
            color = appColors.textSecondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Crear cuenta",
                    color = TextPrimary,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
                CustomTextField(
                    value = nombreUsuario,
                    onValueChange = {
                        nombreUsuario = it
                        if (uiState is RegisterUiState.Error) viewModel.resetState()
                    },
                    label = "Nombre de usuario",
                    isError = uiState is RegisterUiState.Error
                )
                CustomTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        if (uiState is RegisterUiState.Error) viewModel.resetState()
                    },
                    label = "Email",
                    keyboardType = KeyboardType.Email,
                    isError = uiState is RegisterUiState.Error
                )
                PasswordTextField(
                    value = password,
                    onValueChange = {
                        password = it
                        if (uiState is RegisterUiState.Error) viewModel.resetState()
                    },
                    label = "Contraseña",
                    isError = uiState is RegisterUiState.Error
                )
                PasswordTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                        if (uiState is RegisterUiState.Error) viewModel.resetState()
                    },
                    label = "Confirmar contraseña",
                    isError = uiState is RegisterUiState.Error,
                    errorMessage = if (uiState is RegisterUiState.Error)
                        (uiState as RegisterUiState.Error).message
                    else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                PrimaryButton(
                    text = "Crear cuenta",
                    onClick = {
                        viewModel.register(
                            nombreUsuario,
                            email,
                            password,
                            confirmPassword
                        )
                    },
                    isLoading = uiState is RegisterUiState.Loading
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) {
            Text(
                text = "¿Ya tienes cuenta? Inicia sesión",
                color = PrimaryBlue,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}