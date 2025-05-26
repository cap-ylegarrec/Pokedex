package com.legarrec.pokedex.pokemon

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.legarrec.pokedex.R
import com.legarrec.pokedex.ui.backgroundWithColors
import pokemon.presentation.PokemonUI
import pokemon.presentation.StatsUI
import pokemon.presentation.TypeUI

@Composable
fun PokemonViewItem(
    pokemon: PokemonUI,
    modifier: Modifier = Modifier,
    onSpeak: ((String) -> Unit)?,
    onClick: (PokemonUI) -> Unit
) {
    val backgroundModifier = modifier.backgroundWithColors(pokemon.backgroundColorList)

    Row(
        modifier = backgroundModifier
            .clickable(enabled = true) { onClick.invoke(pokemon) }
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "#${pokemon.id}",
                style = MaterialTheme.typography.h6,
            )
            Text(
                text = pokemon.name,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(
                modifier = Modifier.size(40.dp),
                onClick = { onSpeak?.invoke(pokemon.name) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sound),
                    contentDescription = "Speak"
                )
            }
            AsyncImage(
                model = pokemon.spriteUrl,
                contentDescription = "Pokemon Image",
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.pokeball),
                error = painterResource(R.drawable.honorball)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewItemViewMonoColor() {
    PokemonViewItem(
        pokemon = PokemonUI(
            id = 1,
            name = "Pikachu",
            listOf("#FFD700"),
            listOf(
                TypeUI("Électrik", "#FFD700", "")
            ),
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/25.png",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        modifier = Modifier.fillMaxWidth(),
        onSpeak = {},
        onClick = {}
    )
}

@Suppress("SpellCheckingInspection")
@Preview(showBackground = true)
@Composable
fun PreviewItemViewTwoColor() {
    PokemonViewItem(
        pokemon = PokemonUI(
            id = 1,
            name = "Bulbasaur",
            listOf("#8A2BE2", "#32CD32"),
            listOf(
                TypeUI("Poison", "#8A2BE2", ""),
                TypeUI("Plante", "#32CD32", "")
            ),
            "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        modifier = Modifier.fillMaxWidth(),
        onSpeak = {},
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewItemViewMultiColor() {
    PokemonViewItem(
        pokemon = PokemonUI(
            id = 1, name = "Test", listOf("#FF4500", "#8A2BE2", "#32CD32"), listOf(
                TypeUI("Poison", "#FF4500", ""),
                TypeUI("Plante", "#8A2BE2", ""),
                TypeUI("Feu", "#32CD32", "")
            ), "",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        modifier = Modifier.fillMaxWidth(),
        onSpeak = {},
        onClick = {}
    )
}
