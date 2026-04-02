package com.example.app_futbol_tfg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.app_futbol_tfg.ui.ui.theme.App_Futbol_TFGTheme
import androidx.lifecycle.lifecycleScope
import com.example.app_futbol_tfg.data.database.DatabaseProvider
import com.example.app_futbol_tfg.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import com.example.app_futbol_tfg.ui.screens.addmatch.AddMatchScreen
import com.example.app_futbol_tfg.ui.screens.map.MapScreen
import com.example.app_futbol_tfg.ui.screens.stats.StatsScreen
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import com.example.app_futbol_tfg.data.database.AppDatabase
import com.example.app_futbol_tfg.ui.screens.addmatch.AddMatchScreen
import com.example.app_futbol_tfg.ui.screens.home.HomeScreen
import com.example.app_futbol_tfg.ui.screens.map.MapScreen
import com.example.app_futbol_tfg.ui.screens.matchdetail.MatchDetailScreen
import com.example.app_futbol_tfg.ui.screens.stats.StatsScreen

private const val DEMO_USER_ID = 2
// Definimos las posibles pantallas que vamos a utilizar
sealed interface AppScreen {
    data object Home : AppScreen
    data object AddMatch : AppScreen
    data object Stats : AppScreen
    data object Map : AppScreen
    data class MatchDetail(val matchId: Int) : AppScreen // Un id que enviaremos desde AddMatch
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val db = DatabaseProvider.getDatabase(applicationContext)
        lifecycleScope.launch {
            val pais = db.paisDao().getById(1)
            println("Pais 1: $pais")
        }
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
                    currentScreen = AppScreen.MatchDetail(matchId)
                }
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
                    currentScreen = AppScreen.AddMatch
                },
                onMatchAdded = {
                    currentScreen = AppScreen.Home
                }
            )
        }
    }
}