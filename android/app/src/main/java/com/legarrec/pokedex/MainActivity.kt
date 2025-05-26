package com.legarrec.pokedex

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.legarrec.pokedex.pokemon.PokemonListView
import com.legarrec.pokedex.pokemon.PokemonView
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokedexApp()
        }
    }
}

@Composable
fun PokedexApp() {

    // --- TTS setup ---
    val context = LocalContext.current
    val tts = rememberTextToSpeech(context)
    DisposableEffect(Unit) {
        onDispose { tts.shutdown() }
    }
    // --- End TTS setup ---


    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.systemBarsPadding(),
        navController = navController,
        startDestination = "list"
    ) {
        composable("list") {
            PokemonListView(
                onItemClick = { pokemon ->
                    navController.navigateToDetails(pokemon.id, null)
                },
                onSpeak = { name ->
                    tts.speak(name, TextToSpeech.QUEUE_FLUSH, null, null)
                }
            )
        }
        composable(
            route = "details/{pokemonId}/tab/{selectedTab}",
            arguments = listOf(navArgument("pokemonId") {
                type = NavType.LongType; nullable = false
            },navArgument("selectedTab") {
                type = NavType.StringType; nullable = true
            })
        ) { backStackEntry ->
            val pokemonId = backStackEntry.arguments?.getLong("pokemonId")
            val selectedTab = backStackEntry.arguments?.getString("selectedTab")
            if (pokemonId != null && pokemonId != 0L) {
                PokemonView(
                    pokemonId, onBack = { navController.popBackStack() },
                    selectedTab = selectedTab,
                    onItemClick = { pokemon ->
                        navController.navigateToDetails(pokemon.id, selectedTab = "Evolution")
                    },
                    onSpeak = { name ->
                        tts.speak(name, TextToSpeech.QUEUE_FLUSH, null, null)
                    })
            } else {
                Text("No Pokémon data. Please return to the list.")
            }
        }
    }
}

@Composable
private fun rememberTextToSpeech(context: Context): TextToSpeech {
    return remember {
        TextToSpeech(context) { status ->
            Log.d("TTS", "TextToSpeech initialized with status: $status")
        }.apply {
            language = Locale.getDefault()
        }
    }
}

private fun NavController.navigateToDetails(pokemonId: Long, selectedTab: String?) {
    this.navigate("details/$pokemonId/tab/$selectedTab") {
        popUpTo("list")
        launchSingleTop = true
    }
}
