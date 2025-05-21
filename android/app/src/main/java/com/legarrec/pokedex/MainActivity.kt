package com.legarrec.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.legarrec.pokedex.di.appModule
import di.commonModule
import com.legarrec.pokedex.pokemon.PokemonListView
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Koin
        startKoin {
            androidContext(this@MainActivity)
            modules(commonModule, appModule)
        }

        setContent {
            PokedexApp()
        }
    }
}

@Composable
fun PokedexApp() {
    Scaffold(
        Modifier.systemBarsPadding(),
        topBar = {
            TopAppBar(title = { Text("Pokedex") })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            PokemonListView()
        }
    }
}
