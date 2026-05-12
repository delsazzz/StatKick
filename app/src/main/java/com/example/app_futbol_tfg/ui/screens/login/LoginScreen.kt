package com.example.app_futbol_tfg.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Image
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.BackgroundLight
import com.example.app_futbol_tfg.ui.ui.theme.CardBackground
import com.example.app_futbol_tfg.ui.ui.theme.GreyBlueDark
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryDark
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

@Composable
fun LoginScreen() {
    // Estados temporales solo para mostrar el contenido escrito en pantalla.
    // No hay lógica real todavía; más adelante esto lo podéis mover al ViewModel.
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        BackgroundLight, // Gris azulado clarito
                        GreyBlueDark, // Gris azulado oscuro
                    )
                )
            )
            .safeDrawingPadding()
    ) {
        // Variables para adaptar el tamaño a la pantalla
        val isSmallScreen = maxHeight < 700.dp
        val logoSize = if (isSmallScreen) 90.dp else 120.dp
        val titleSize = if (isSmallScreen) 24.sp else 30.sp
        val subtitleSize = if (isSmallScreen) 13.sp else 15.sp
        val cardPadding = if (isSmallScreen) 20.dp else 28.dp
        val verticalSpacing = if (isSmallScreen) 14.dp else 20.dp
        val cardWidthFraction = if (maxWidth < 500.dp) 0.92f else 0.72f

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Contenedor principal de login
            Card(
                modifier = Modifier
                    .fillMaxWidth(cardWidthFraction)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CardBackground
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(cardPadding),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo de la app",
                        modifier = Modifier.size(logoSize),
                        contentScale = ContentScale.Fit
                    )
                    // Espacio vertical para separar contenido
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    Text(
                        text = "Iniciar sesión",
                        color = TextPrimary,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontSize = titleSize,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Accede a tu espacio de fútbol y estadísticas",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = subtitleSize
                        )
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing + 4.dp))
                    // Campo email / usuario
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Correo o usuario")
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = TextSecondary,
                            cursorColor = PrimaryBlue,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    // Campo contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = {
                            Text(text = "Contraseña")
                        },
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryBlue,
                            unfocusedBorderColor = Color(0xFFCBD5E1),
                            focusedLabelColor = PrimaryBlue,
                            unfocusedLabelColor = TextSecondary,
                            cursorColor = PrimaryBlue,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    Spacer(modifier = Modifier.height(verticalSpacing + 6.dp))
                    // Botón principal
                    Button(
                        onClick = {
                            // De momento sin lógica
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Entrar",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(verticalSpacing))
                    // Texto secundario inferior
                    Text(
                        text = "Pantalla visual preparada para aplicar la lógica más adelante",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontSize = 12.sp
                        )
                    )
                }
            }
        }
    }
}