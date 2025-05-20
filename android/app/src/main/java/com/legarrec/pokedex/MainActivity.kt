package com.legarrec.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.legarrec.pokedex.item.ItemListView

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
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Pokedex") })
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            ItemListView()
        }
    }
}
