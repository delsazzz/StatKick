package com.example.app_futbol_tfg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de registro
sealed class RegisterUiState {
    // Estado inicial, esperando interacción del usuario
    object Idle : RegisterUiState()
    // Cargando, mientras se procesa el registro
    object Loading : RegisterUiState()
    // Registro correcto, devuelve el id del nuevo usuario
    data class Success(val userId: Int) : RegisterUiState()
    // Error con mensaje descriptivo para mostrar en la UI
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(private val authRepository: AuthRepository) : ViewModel() {

    // Estado observable de la UI
    private val _uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.Idle)
    val uiState: StateFlow<RegisterUiState> = _uiState

    // Intenta registrar un nuevo usuario con los datos introducidos
    fun register(nombreUsuario: String, email: String, password: String, confirmPassword: String) {
        // Validaciones antes de llamar al repositorio
        // Validaciones del nombre de usuario
        if (nombreUsuario.isBlank()) {
            _uiState.value = RegisterUiState.Error("El nombre de usuario no puede estar vacío")
            return
        }
        if (nombreUsuario.length < 3) {
            _uiState.value = RegisterUiState.Error("El nombre de usuario debe tener al menos 3 caracteres")
            return
        }
        if (nombreUsuario.length > 20) {
            _uiState.value = RegisterUiState.Error("El nombre de usuario no puede tener más de 20 caracteres")
            return
        }
        if (!nombreUsuario.matches(Regex("^[a-zA-Z0-9_]+$"))) {
            _uiState.value = RegisterUiState.Error("El nombre de usuario solo puede contener letras, números y guiones bajos")
            return
        }

// Validaciones del email
        if (email.isBlank()) {
            _uiState.value = RegisterUiState.Error("El email no puede estar vacío")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = RegisterUiState.Error("El formato del email no es válido")
            return
        }
        if (email.length > 100) {
            _uiState.value = RegisterUiState.Error("El email no puede tener más de 100 caracteres")
            return
        }
        if (password.isBlank()) {
            _uiState.value = RegisterUiState.Error("La contraseña no puede estar vacía")
            return
        }
        if (password.length < 8) {
            _uiState.value = RegisterUiState.Error("La contraseña debe tener al menos 8 caracteres")
            return
        }
        if (password.length > 32) {
            _uiState.value = RegisterUiState.Error("La contraseña no puede tener más de 32 caracteres")
            return
        }
        if (!password.any { it.isUpperCase() }) {
            _uiState.value = RegisterUiState.Error("La contraseña debe contener al menos una mayúscula")
            return
        }
        if (!password.any { it.isLowerCase() }) {
            _uiState.value = RegisterUiState.Error("La contraseña debe contener al menos una minúscula")
            return
        }
        if (!password.any { it.isDigit() }) {
            _uiState.value = RegisterUiState.Error("La contraseña debe contener al menos un número")
            return
        }
        if (!password.any { it in "!@#\$%^&*()_+-=[]{}|;':\",./<>?" }) {
            _uiState.value = RegisterUiState.Error("La contraseña debe contener al menos un carácter especial")
            return
        }
        if (password == nombreUsuario) {
            _uiState.value = RegisterUiState.Error("La contraseña no puede ser igual al nombre de usuario")
            return
        }
        if (password != confirmPassword) {
            _uiState.value = RegisterUiState.Error("Las contraseñas no coinciden")
            return
        }
        viewModelScope.launch {
            _uiState.value = RegisterUiState.Loading
            val usuario = authRepository.register(nombreUsuario, email, password)
            _uiState.value = if (usuario != null) {
                RegisterUiState.Success(usuario.id)
            } else {
                RegisterUiState.Error("El email ya está registrado")
            }
        }
    }

    // Resetea el estado a Idle para limpiar errores
    fun resetState() {
        _uiState.value = RegisterUiState.Idle
    }
}