package com.example.app_futbol_tfg.data

import android.content.Context

// Gestiona la sesión del usuario usando SharedPreferences.
// Permite recordar qué usuario está logueado entre sesiones.
class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences(
        "futbol_tfg_session",
        Context.MODE_PRIVATE
    )

    companion object {
        private const val KEY_USER_ID = "user_id"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"

        private const val KEY_DARK_MODE = "dark_mode"
        private const val NO_USER = -1
    }

    // Guarda la sesión del usuario al iniciar sesión
    fun saveSession(userId: Int) {
        prefs.edit()
            .putInt(KEY_USER_ID, userId)
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }

    // Elimina la sesión al cerrar sesión
    fun clearSession() {
        prefs.edit()
            .putInt(KEY_USER_ID, NO_USER)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }

    // Devuelve el id del usuario logueado o -1 si no hay sesión
    fun getUserId(): Int = prefs.getInt(KEY_USER_ID, NO_USER)

    // Indica si hay una sesión activa
    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, false)

    // Guarda la preferencia de tema oscuro
    fun setDarkMode(enabled: Boolean) {
        prefs.edit()
            .putBoolean(KEY_DARK_MODE, enabled)
            .apply()
    }

    // Devuelve si el tema oscuro está activado
    fun isDarkMode(): Boolean = prefs.getBoolean(KEY_DARK_MODE, false)
}