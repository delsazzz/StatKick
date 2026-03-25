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
                MapScreen()
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    App_Futbol_TFGTheme {
        Greeting("Android")
    }
}
}