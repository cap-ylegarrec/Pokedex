package pokemon.presentation.pokemonList

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import core.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.presentation.PokemonUI

class PokemonListViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PokemonListState())

    @NativeCoroutinesState
    val state: StateFlow<PokemonListState> = _state.asStateFlow()

    private var currentGeneration = 1
    private val loadedGenerations = mutableSetOf<Int>()

    fun handleIntent(intent: PokemonListIntent) {
        when (intent) {
            is PokemonListIntent.LoadPokemonList -> viewModelScope.launch {
                loadPokemonList()
            }
        }
    }

    private suspend fun loadPokemonList() {
        if (_state.value.pokemonUIList.isEmpty()) {
            _state.value = PokemonListState(isLoading = true)
            val pokemonList = getPokemonListUseCase.execute(1)
            _state.value = PokemonListState(
                pokemonUIList = pokemonList.map { pokemon -> PokemonUI.from(pokemon) },
                isLoading = false
            )
        }
    }

    fun loadNextGenerationIfNeeded() {
        viewModelScope.launch {
            val currentList = state.value.pokemonUIList
            if (!loadedGenerations.contains(currentGeneration + 1)) {
                val newPokemon = getPokemonListUseCase.execute(currentGeneration + 1)
                if (newPokemon.isNotEmpty()) {
                    currentGeneration++
                    loadedGenerations.add(currentGeneration)
                    _state.value = PokemonListState(
                        pokemonUIList = currentList + newPokemon.map { PokemonUI.from(it) },
                        isLoading = false
                    )
                }
            }
        }
    }
}