package com.legarrec.pokedex.pokemon

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.legarrec.pokedex.R
import org.koin.compose.koinInject // Updated import for Koin
import pokemon.presentation.PokemonUI
import pokemon.presentation.StatsUI
import pokemon.presentation.TypeUI
import pokemon.presentation.pokemonList.PokemonListIntent
import pokemon.presentation.pokemonList.PokemonListViewModel
import kotlin.collections.listOf

@Composable
fun PokemonListView(
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: (String) -> Unit,
) {
    val viewModel: PokemonListViewModel = koinInject()
    val state by viewModel.state.collectAsState()
    val searchedPosition by viewModel.searchedPokemonPosition.collectAsState()
    val listState = rememberLazyListState()

    val speechRecognizerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            spokenText?.let {
                viewModel.handleIntent(PokemonListIntent.SearchPokemon(it))
            }
        }
    }

    // Trigger loading items
    LaunchedEffect(Unit) {
        viewModel.handleIntent(PokemonListIntent.LoadPokemonList)
    }

    // Detect when reaching the end of the list
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == listState.layoutInfo.totalItemsCount - 1 }
            .collect { isAtEnd ->
                if (isAtEnd) {
                    viewModel.handleIntent(PokemonListIntent.LoadNextGeneration)
                }
            }
    }

    // Scroll to searched position when it changes
    LaunchedEffect(searchedPosition) {
        searchedPosition?.let {
            listState.animateScrollToItem(it)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        PokemonListContent(
            pokemonList = state.pokemonUIList,
            onItemClick = onItemClick,
            listState = listState,
            onSpeak = onSpeak,
        )

        FloatingActionButton(
            onClick = {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE, "fr-FR")
                    putExtra(
                        RecognizerIntent.EXTRA_PROMPT,
                        "Donne le nom du Pokémon que tu cherches"
                    )
                }
                speechRecognizerLauncher.launch(intent)
            },
            backgroundColor = Color.Blue,
            contentColor = Color.White,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_microphone),
                contentDescription = "Microphone"
            )
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
    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(top = 80.dp)
    ) {
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
