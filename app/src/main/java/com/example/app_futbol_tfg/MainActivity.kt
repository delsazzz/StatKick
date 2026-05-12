package com.example.app_futbol_tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.data.database.DatabaseProvider
import com.example.app_futbol_tfg.ui.screens.addmatch.AddMatchScreen
import com.example.app_futbol_tfg.ui.screens.home.HomeScreen
import com.example.app_futbol_tfg.ui.screens.map.MapScreen
import com.example.app_futbol_tfg.ui.screens.matchdetail.MatchDetailScreen
import com.example.app_futbol_tfg.ui.screens.stats.StatsScreen
import com.example.app_futbol_tfg.ui.screens.totalmatches.TotalMatchesScreen
import com.example.app_futbol_tfg.ui.ui.theme.App_Futbol_TFGTheme

private const val DEMO_USER_ID = 2
// Definimos las pantallas que vamos a utilizar en la aplicación
sealed interface AppScreen {
    data object Home : AppScreen
    data object AddMatch : AppScreen
    data object Stats : AppScreen
    data object Map : AppScreen
    data object TotalMatches : AppScreen
    // En esta pantalla de MatchDetail además del id del partido, se guarda desde
    // que pantalla se abrioó para controlar el botón de volver atrás
    data class MatchDetail(val matchId: Int, val from: DetailOrigin) : AppScreen
}
// Aquí definimos el origen desde dónde se abrió la pantalla MatchDetail
enum class DetailOrigin {
    ADD_MATCH,
    SAVED_MATCHES
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Aquí obtenemos la instancia de la BBDD para toda la app
        val db = DatabaseProvider.getDatabase(applicationContext)
        enableEdgeToEdge()
        setContent {
            App_Futbol_TFGTheme {
                TfgApp(db = db)
        }
    }
}
    @Composable
    fun TfgApp(db: AppDatabase) {
        var currentScreen by remember { mutableStateOf<AppScreen>(AppScreen.Home) }
        // Estas variables se usan para el estado persistente de la bñusqueda en AddMatch
        var addMatchSearchText by rememberSaveable { mutableStateOf("") }
        var addMatchSelectedSuggestionType by rememberSaveable { mutableStateOf<String?>(null) }
        var addMatchSelectedSuggestionId by rememberSaveable { mutableStateOf<Int?>(null) }
        var addMatchShowSuggestions by rememberSaveable { mutableStateOf(false) }

        when (val screen = currentScreen) {
            AppScreen.Home -> HomeScreen(
                userId = DEMO_USER_ID,
                db = db,
                onNavigateBottom = { index ->
                    currentScreen = when (index) {
                        0 -> AppScreen.Home
                        1 -> AppScreen.AddMatch
                        2 -> AppScreen.Stats
                        else -> AppScreen.Map
                    }
                },
                onOpenTotalMatches = {currentScreen = AppScreen.TotalMatches}
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
                userId = DEMO_USER_ID,
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
                userId = DEMO_USER_ID,
                db = db,
                onBack = {
                    currentScreen = AppScreen.Home
                },
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
                }
            )
            is AppScreen.MatchDetail -> MatchDetailScreen(
                matchId = screen.matchId,
                userId = DEMO_USER_ID,
                db = db,
                onBack = {
                    currentScreen = when (screen.from) {
                        DetailOrigin.ADD_MATCH -> AppScreen.AddMatch
                        DetailOrigin.SAVED_MATCHES -> AppScreen.TotalMatches
                    }
                },
            )
        }
    }
}