package com.example.app_futbol_tfg

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.example.app_futbol_tfg.data.SessionManager
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.database.DatabaseProvider
import com.example.app_futbol_tfg.data.initializeAppData
import com.example.app_futbol_tfg.data.repository.ApiFootballRepositoryProvider
import com.example.app_futbol_tfg.data.repository.AuthRepository
import com.example.app_futbol_tfg.ui.screens.addmatch.AddMatchScreen
import com.example.app_futbol_tfg.ui.screens.home.HomeScreen
import com.example.app_futbol_tfg.ui.screens.login.LoginScreen
import com.example.app_futbol_tfg.ui.screens.map.MapScreen
import com.example.app_futbol_tfg.ui.screens.matchdetail.MatchDetailScreen
import com.example.app_futbol_tfg.ui.screens.register.RegisterScreen
import com.example.app_futbol_tfg.ui.screens.splash.SplashScreen
import com.example.app_futbol_tfg.ui.screens.stats.StatsScreen
import com.example.app_futbol_tfg.ui.screens.totalmatches.TotalMatchesScreen
import com.example.app_futbol_tfg.ui.ui.theme.App_Futbol_TFGTheme
import com.example.app_futbol_tfg.ui.ui.theme.DarkAppColors
import com.example.app_futbol_tfg.ui.ui.theme.LightAppColors
import com.example.app_futbol_tfg.ui.ui.theme.LocalAppColors
import com.example.app_futbol_tfg.ui.viewmodels.BaseViewModelFactory
import com.example.app_futbol_tfg.ui.viewmodels.LoginViewModel
import com.example.app_futbol_tfg.ui.viewmodels.RegisterViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.app_futbol_tfg.ui.screens.editprofile.EditProfileScreen
// Define todas las pantallas disponibles dentro de la navegación de la aplicación
sealed interface AppScreen {
    data object Splash : AppScreen
    data object Login : AppScreen
    data object Register : AppScreen
    data object Home : AppScreen
    data object EditProfile : AppScreen
    data object AddMatch : AppScreen
    data object Stats : AppScreen
    data object Map : AppScreen
    data object TotalMatches : AppScreen
    // MatchDetail recibe información extra para saber desde donde se abrió (AddMatch o TotalMatches)
    data class MatchDetail(val matchId: Int, val from: DetailOrigin) : AppScreen
}
// Controla el comportamiento del botón de atrás en MatchDetail
enum class DetailOrigin {
    ADD_MATCH,
    SAVED_MATCHES
}
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inicialización de la BBDD local
        val db = DatabaseProvider.getDatabase(applicationContext)
        // Gestor encargado de mantener la sesión del usuario
        val sessionManager = SessionManager(applicationContext)
        // Repositorio utilizado para login y registro
        val authRepository = AuthRepository(db, sessionManager)
        // Inicialización del ViewModel de login mediante factory personalizada
        val loginViewModel: LoginViewModel by viewModels {
            BaseViewModelFactory(LoginViewModel::class.java) {
                LoginViewModel(authRepository)
            }
        }
        // Inicialización del ViewModel de registro
        val registerViewModel: RegisterViewModel by viewModels {
            BaseViewModelFactory(RegisterViewModel::class.java) {
                RegisterViewModel(authRepository)
            }
        }
        // Carga inicial de datos desde la API-Football en segundo plano
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiRepo = ApiFootballRepositoryProvider.getInstance(db)
                initializeAppData(apiRepo, BuildConfig.API_FOOTBALL_KEY, db)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error en carga inicial", e)
            }
        }
        // Permite que la aplicación ocupe toda la pantalla
        enableEdgeToEdge()
        setContent {
            MainContent(
                db = db,
                sessionManager = sessionManager,
                loginViewModel = loginViewModel,
                registerViewModel = registerViewModel
            )
        }
    }
}

@Composable
fun MainContent(
    db: AppDatabase,
    sessionManager: SessionManager,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel
) {
    // Estado utilizado para controlar el modo claro y oscuro
    var isDarkMode by remember { mutableStateOf(sessionManager.isDarkMode()) }
    val appColors = if (isDarkMode) DarkAppColors else LightAppColors
    // Proporciona la paleta de colores personalizada al resto de composables
    CompositionLocalProvider(LocalAppColors provides appColors) {
        App_Futbol_TFGTheme(darkTheme = isDarkMode) {
            TfgApp(
                db = db,
                sessionManager = sessionManager,
                loginViewModel = loginViewModel,
                registerViewModel = registerViewModel,
                isDarkMode = isDarkMode,
                // Cambia el tema de la aplicación y guarda la preferencia
                onToggleDarkMode = {
                    isDarkMode = !isDarkMode
                    sessionManager.setDarkMode(isDarkMode)
                }
            )
        }
    }
}

@Composable
fun TfgApp(
    db: AppDatabase,
    sessionManager: SessionManager,
    loginViewModel: LoginViewModel,
    registerViewModel: RegisterViewModel,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit
) {
    // Controla la pantalla actual mostrada en la aplicación
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }
    // Mantiene el usuario autenticado durante la sesión abierta
    var currentUserId by remember { mutableIntStateOf(sessionManager.getUserId()) }
    // Estados persistentes utilizados en el buscador de AddMatch
    var addMatchSearchText by rememberSaveable { mutableStateOf("") }
    var addMatchSelectedSuggestionType by rememberSaveable { mutableStateOf<String?>(null) }
    var addMatchSelectedSuggestionId by rememberSaveable { mutableStateOf<Int?>(null) }
    var addMatchShowSuggestions by rememberSaveable { mutableStateOf(false) }
    // Cierra la sesión y devuelve al login para iniciar de nuevo
    val onLogout: () -> Unit = {
        sessionManager.clearSession()
        currentUserId = -1
        currentScreen = AppScreen.Login
    }
    // Sistema de navegación principal de la aplicación
    when (val screen = currentScreen) {
        AppScreen.Splash -> SplashScreen(
            isLoggedIn = sessionManager.isLoggedIn(),
            onNavigateToHome = {
                currentUserId = sessionManager.getUserId()
                currentScreen = AppScreen.Home
            },
            onNavigateToLogin = { currentScreen = AppScreen.Login }
        )
        AppScreen.Login -> LoginScreen(
            viewModel = loginViewModel,
            onLoginSuccess = { userId ->
                currentUserId = userId
                currentScreen = AppScreen.Home
            },
            onNavigateToRegister = { currentScreen = AppScreen.Register }
        )
        AppScreen.Register -> RegisterScreen(
            viewModel = registerViewModel,
            onRegisterSuccess = { userId ->
                currentUserId = userId
                currentScreen = AppScreen.Home
            },
            onNavigateToLogin = { currentScreen = AppScreen.Login }
        )
        AppScreen.Home -> HomeScreen(
            userId = currentUserId,
            db = db,
            onNavigateBottom = { index ->
                currentScreen = when (index) {
                    0 -> AppScreen.Home
                    1 -> AppScreen.AddMatch
                    2 -> AppScreen.Stats
                    else -> AppScreen.Map
                }
            },
            onOpenTotalMatches = { currentScreen = AppScreen.TotalMatches },
            onLogout = onLogout,
            onToggleDarkMode = onToggleDarkMode,
            onEditProfile = {
                currentScreen = AppScreen.EditProfile
            },
            isDarkMode = isDarkMode
        )
        AppScreen.EditProfile -> EditProfileScreen(
            userId = currentUserId,
            db = db,
            onBack = {
                currentScreen = AppScreen.Home
            }
        )
        AppScreen.AddMatch -> AddMatchScreen(
            db = db,
            onNavigateBottom = { index ->
                currentScreen = when (index) {
                    0 -> AppScreen.Home
                    1 -> AppScreen.AddMatch
                    2 -> AppScreen.Stats
                    else -> AppScreen.Map
                }
            },
            onOpenMatchDetail = { matchId ->
                currentScreen = AppScreen.MatchDetail(
                    matchId = matchId,
                    from = DetailOrigin.ADD_MATCH
                )
            },
            searchText = addMatchSearchText,
            onSearchTextChange = { addMatchSearchText = it },
            selectedSuggestionType = addMatchSelectedSuggestionType,
            onSelectedSuggestionTypeChange = { addMatchSelectedSuggestionType = it },
            selectedSuggestionId = addMatchSelectedSuggestionId,
            onSelectedSuggestionIdChange = { addMatchSelectedSuggestionId = it },
            showSuggestions = addMatchShowSuggestions,
            onShowSuggestionsChange = { addMatchShowSuggestions = it }
        )
        AppScreen.Stats -> StatsScreen(
            userId = currentUserId,
            db = db,
            onNavigateBottom = { index ->
                currentScreen = when (index) {
                    0 -> AppScreen.Home
                    1 -> AppScreen.AddMatch
                    2 -> AppScreen.Stats
                    else -> AppScreen.Map
                }
            }
        )
        AppScreen.TotalMatches -> TotalMatchesScreen(
            userId = currentUserId,
            db = db,
            onBack = { currentScreen = AppScreen.Home },
            onOpenMatchDetail = { matchId ->
                currentScreen = AppScreen.MatchDetail(
                    matchId = matchId,
                    from = DetailOrigin.SAVED_MATCHES
                )
            }
        )
        AppScreen.Map -> MapScreen(
            onNavigateBottom = { index ->
                currentScreen = when (index) {
                    0 -> AppScreen.Home
                    1 -> AppScreen.AddMatch
                    2 -> AppScreen.Stats
                    else -> AppScreen.Map
                }
            },
            userId = currentUserId
        )
        is AppScreen.MatchDetail -> MatchDetailScreen(
            matchId = screen.matchId,
            userId = currentUserId,
            db = db,
            onBack = {
                currentScreen = when (screen.from) {
                    DetailOrigin.ADD_MATCH -> AppScreen.AddMatch
                    DetailOrigin.SAVED_MATCHES -> AppScreen.TotalMatches
                }
            }
        )
    }
}