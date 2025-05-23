package com.legarrec.pokedex.pokemon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pokemon.presentation.PokemonUI
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.legarrec.pokedex.R

@Composable
fun PokemonView(pokemon: PokemonUI, modifier: Modifier = Modifier) {
    val colors = pokemon.colorList.mapNotNull { runCatching { Color(it.toColorInt()) }.getOrNull() }
    val backgroundModifier = when {
        colors.size == 1 -> modifier.background(colors.first())
        colors.size > 1 -> {
            val stops = colors.indices.map { it.toFloat() / (colors.size - 1) }.toTypedArray()
            modifier.background(
                Brush.linearGradient(
                    colorStops = stops.zip(colors).toTypedArray(),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 0f), // Horizontal gradient
                    tileMode = TileMode.Clamp
                )
            )
        }

        else -> modifier.background(Color.White)
    }

    Row(
        modifier = backgroundModifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "#${pokemon.id}",
                style = MaterialTheme.typography.h6,
                color = Color.Gray,
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.h6
            )
        }
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            placeholder = painterResource(R.drawable.pokeball),
            error = painterResource(R.drawable.honorball),
            modifier = Modifier
                .size(56.dp)
                .padding(start = 12.dp),
            contentScale = ContentScale.Fit,
            onError = { e -> Log.d("PokemonView", "Error loading image: ${e.result}") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemViewMonoColor() {
    PokemonView(
        pokemon = PokemonUI(
            id = 1,
            name = "Pikachu",
            listOf("#FFD700"),
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png"
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Suppress("SpellCheckingInspection")
@Preview(showBackground = true)
@Composable
fun PreviewItemViewTwoColor() {
    PokemonView(
        pokemon = PokemonUI(
            id = 1,
            name = "Bulbasaur",
            listOf("#8A2BE2", "#32CD32"),
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png"
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemViewMultiColor() {
    PokemonView(
        pokemon = PokemonUI(id = 1, name = "Test", listOf("#FF4500", "#8A2BE2", "#32CD32"), ""),
        modifier = Modifier.fillMaxWidth()
    )
}
