package com.legarrec.pokedex.pokemon

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject // Updated import for Koin
import pokemon.presentation.PokemonIntent
import pokemon.presentation.PokemonViewModel
import pokemon.domain.model.Pokemon
import pokemon.presentation.PokemonUI
import kotlin.collections.listOf

@Composable
fun PokemonListView() {
    val viewModel: PokemonViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    PokemonListContent(pokemonList = state.pokemonUIList)

    // Trigger loading items
    LaunchedEffect(Unit) {
        viewModel.handleIntent(PokemonIntent.LoadPokemonList)
    }
}

@Composable
private fun PokemonListContent(pokemonList: List<PokemonUI>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(pokemonList) { pokemon ->
            PokemonView(
                pokemon = pokemon,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Suppress("SpellCheckingInspection")
@Preview(showBackground = true)
@Composable
fun PreviewItemListView() {
    val fakePokemonList = listOf(
        PokemonUI(1, "Bulbasaur", listOf("#78C850"), ""),
        PokemonUI(4, "Charmander", listOf("#F08030"), ""),
        PokemonUI(7, "Squirtle", listOf("#6890F0"), ""),
        PokemonUI(id = 1, name = "Pikachu", listOf("#FFD700"), "")
    )
    PokemonListContent(pokemonList = fakePokemonList)
}
