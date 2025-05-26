package com.legarrec.pokedex.pokemon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject // Updated import for Koin
import pokemon.presentation.pokemonList.PokemonListIntent
import pokemon.presentation.pokemonList.PokemonListViewModel
import pokemon.presentation.PokemonUI
import pokemon.presentation.StatsUI
import pokemon.presentation.TypeUI
import kotlin.collections.listOf

@Composable
fun PokemonListView(
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: (String) -> Unit,
) {
    val viewModel: PokemonListViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val listState = rememberLazyListState()

    PokemonListContent(
        pokemonList = state.pokemonUIList,
        onItemClick = onItemClick,
        listState = listState,
        onSpeak = onSpeak
    )

    // Trigger loading items
    LaunchedEffect(Unit) {
        viewModel.handleIntent(PokemonListIntent.LoadPokemonList)
    }

    // Detect when reaching the end of the list
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1 }
            .collect { isAtEnd ->
                if (isAtEnd) {
                    viewModel.loadNextGenerationIfNeeded()
                }
            }
    }
}

@Composable
private fun PokemonListContent(
    pokemonList: List<PokemonUI>,
    onSpeak: (String) -> Unit,
    onItemClick: (PokemonUI) -> Unit,
    listState: LazyListState
) {
    LazyColumn(modifier = Modifier.fillMaxSize(), state = listState) {
        items(pokemonList) { pokemon ->
            PokemonViewItem(
                pokemon = pokemon,
                modifier = Modifier.fillMaxWidth(),
                onSpeak = onSpeak,
                onClick = onItemClick
            )
        }
    }
}

@Suppress("SpellCheckingInspection")
@Preview(showBackground = true)
@Composable
fun PreviewItemListView() {
    val fakePokemonList = listOf(
        PokemonUI(
            1, "Bulbasaur", listOf("#78C850"), listOf(
                TypeUI("Poison", "#8A2BE2", ""),
                TypeUI("Plante", "#32CD32", "")
            ), "",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        PokemonUI(
            4, "Charmander", listOf("#F08030"), listOf(
                TypeUI("Feu", "#FF4500", "")
            ), "",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        PokemonUI(
            7, "Squirtle", listOf("#6890F0"), listOf(
                TypeUI("Eau", "#1E90FF", "")
            ), "",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        PokemonUI(
            id = 1, name = "Pikachu", listOf("#FFD700"), listOf(
                TypeUI("Électrik", "#FFD700", "")
            ), "",
            "",
            previousEvolution = listOf(),
            nextEvolution = listOf(),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        )
    )
    PokemonListContent(
        pokemonList = fakePokemonList, onSpeak = {}, onItemClick = {},
        listState = rememberLazyListState()
    )
}
