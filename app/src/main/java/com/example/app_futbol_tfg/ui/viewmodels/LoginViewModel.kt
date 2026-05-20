package com.example.app_futbol_tfg.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.app_futbol_tfg.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Estados posibles de la pantalla de login
sealed class LoginUiState {
    // Estado inicial, esperando interacción del usuario
    object Idle : LoginUiState()
    // Estado mostrado mientras se valida el inicio de sesión
    object Loading : LoginUiState()
    // Login correcto, devuelve el id del usuario
    data class Success(val userId: Int) : LoginUiState()
    // Error con mensaje descriptivo para mostrar en la UI
    data class Error(val message: String) : LoginUiState()
}
class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {
    // Estado observable de la UI mediante StateFlow
    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState
    // Intenta iniciar sesión con las credenciales introducidas
    fun login(email: String, password: String) {
        // Validaciones antes de llamar al repositorio
        if (email.isBlank()) {
            _uiState.value = LoginUiState.Error("El email no puede estar vacío")
            return
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _uiState.value = LoginUiState.Error("El email no tiene un formato válido")
            return
        }
        if (password.isBlank()) {
            _uiState.value = LoginUiState.Error("La contraseña no puede estar vacía")
            return
        }
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            val usuario = authRepository.login(email, password)
            _uiState.value = if (usuario != null) {
                LoginUiState.Success(usuario.id)
            } else {
                LoginUiState.Error("Email o contraseña incorrectos")
            }
        }
    }
    // Resetea al estado inicial para limpiar errores
    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }
}