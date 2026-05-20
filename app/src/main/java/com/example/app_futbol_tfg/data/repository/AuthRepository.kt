package com.example.app_futbol_tfg.data.repository

import com.example.app_futbol_tfg.data.SessionManager
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.entity.UsuarioEntity

// Repositorio que gestiona la autenticación de usuarios.
// Accede a Room para validar credenciales y guarda la sesión en SharedPreferences mediante SessionManager.
class AuthRepository(
    private val db: AppDatabase,
    private val sessionManager: SessionManager
) {
    // Valida las credenciales del usuario e inicia sesión si son correctas
    suspend fun login(email: String, password: String): UsuarioEntity? {
        return try {
            val usuario = db.usuarioDao().getByEmail(email)
            // Comprueba que existe y que la contraseña coincide
            if (usuario != null && usuario.passwordHash == password) {
                // Guarda la sesión para que persista entre sesiones
                sessionManager.saveSession(usuario.id)
                usuario
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    // Registra un nuevo usuario en Room y guarda la sesión
    suspend fun register(
        nombreUsuario: String,
        email: String,
        password: String
    ): UsuarioEntity? {
        return try {
            val existente = db.usuarioDao().getByEmail(email)
            if (existente != null) return null
            val nuevoUsuario = UsuarioEntity(
                nombreUsuario = nombreUsuario,
                email = email,
                passwordHash = password,
                fechaRegistro = java.text.SimpleDateFormat(
                    "yyyy-MM-dd",
                    java.util.Locale.getDefault()
                ).format(java.util.Date())
            )
            val id = db.usuarioDao().insert(nuevoUsuario)
            val usuarioCreado = nuevoUsuario.copy(id = id.toInt())
            // Guardamos la sesión automáticamente tras el registro
            sessionManager.saveSession(usuarioCreado.id)
            usuarioCreado
        } catch (e: Exception) {
            null
        }
    }
    // Cierra la sesión del usuario actual
    fun logout() {
        sessionManager.clearSession()
    }
    // Comprueba si hay una sesión activa
    fun isLoggedIn(): Boolean = sessionManager.isLoggedIn()
    // Devuelve el id del usuario logueado
    fun getLoggedUserId(): Int = sessionManager.getUserId()
}