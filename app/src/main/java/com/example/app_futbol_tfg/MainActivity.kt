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
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val db = DatabaseProvider.getDatabase(applicationContext)
        lifecycleScope.launch {
            val pais = db.paisDao().getById(1)
            println("Pais 1: $pais")
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_Futbol_TFGTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
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