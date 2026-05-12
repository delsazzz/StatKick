package com.example.app_futbol_tfg.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.app_futbol_tfg.R
import com.example.app_futbol_tfg.ui.ui.theme.PrimaryBlue
import com.example.app_futbol_tfg.ui.ui.theme.TextPrimary
import com.example.app_futbol_tfg.ui.ui.theme.TextSecondary

// Campo de texto reutilizable para formularios de autenticación
@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
        } else null,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(18.dp),
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
}

// Campo de contraseña con opción de mostrar u ocultar el texto
@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var passwordVisible by remember { mutableStateOf(false) }
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true,
        isError = isError,
        supportingText = if (isError && errorMessage != null) {
            { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
        } else null,
        visualTransformation = if (passwordVisible)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            // Icono para mostrar u ocultar la contraseña
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(
                        id = if (passwordVisible) R.drawable.profile_user
                        else R.drawable.profile_user
                    ),
                    contentDescription = if (passwordVisible) "Ocultar contraseña"
                    else "Mostrar contraseña",
                    tint = TextSecondary
                )
            }
        },
        shape = RoundedCornerShape(18.dp),
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
}

// Botón principal reutilizable con soporte para estado de carga
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            contentColor = Color.White,
            disabledContainerColor = PrimaryBlue.copy(alpha = 0.5f),
            disabledContentColor = Color.White.copy(alpha = 0.5f)
        )
    ) {
        if (isLoading) {
            // Indicador de carga mientras se procesa la petición
            CircularProgressIndicator(
                color = Color.White,
                strokeWidth = 2.dp,
                modifier = Modifier.height(20.dp)
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}