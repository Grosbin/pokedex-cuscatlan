package com.cuscatlan.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.cuscatlan.pokemon.presentation.navigation.PokemonTrainerNavigation
import com.cuscatlan.pokemon.ui.theme.PokecuscatlanTheme
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PokecuscatlanTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PokemonTrainerNavigation(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}