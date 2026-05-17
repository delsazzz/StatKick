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
    data class MatchDetail(val matchId: Int, val from: DetailOrigin) : AppScreen
}

enum class DetailOrigin {
    ADD_MATCH,
    SAVED_MATCHES
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(applicationContext)
        val sessionManager = SessionManager(applicationContext)
        val authRepository = AuthRepository(db, sessionManager)

        val loginViewModel: LoginViewModel by viewModels {
            BaseViewModelFactory(LoginViewModel::class.java) {
                LoginViewModel(authRepository)
            }
        }
        val registerViewModel: RegisterViewModel by viewModels {
            BaseViewModelFactory(RegisterViewModel::class.java) {
                RegisterViewModel(authRepository)
            }
        }

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val apiRepo = ApiFootballRepositoryProvider.getInstance(db)
                initializeAppData(apiRepo, BuildConfig.API_FOOTBALL_KEY, db)
            } catch (e: Exception) {
                Log.e("MainActivity", "Error en carga inicial", e)
            }
        }

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
    var isDarkMode by remember { mutableStateOf(sessionManager.isDarkMode()) }
    val appColors = if (isDarkMode) DarkAppColors else LightAppColors

    CompositionLocalProvider(LocalAppColors provides appColors) {
        App_Futbol_TFGTheme(darkTheme = isDarkMode) {
            TfgApp(
                db = db,
                sessionManager = sessionManager,
                loginViewModel = loginViewModel,
                registerViewModel = registerViewModel,
                isDarkMode = isDarkMode,
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
    var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Splash) }
    var currentUserId by remember { mutableStateOf(sessionManager.getUserId()) }
    var addMatchSearchText by rememberSaveable { mutableStateOf("") }
    var addMatchSelectedSuggestionType by rememberSaveable { mutableStateOf<String?>(null) }
    var addMatchSelectedSuggestionId by rememberSaveable { mutableStateOf<Int?>(null) }
    var addMatchShowSuggestions by rememberSaveable { mutableStateOf(false) }

    val onLogout: () -> Unit = {
        sessionManager.clearSession()
        currentUserId = -1
        currentScreen = AppScreen.Login
    }

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