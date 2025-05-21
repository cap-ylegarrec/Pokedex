package pokemon.presentation

import core.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pokemon.domain.usecase.GetPokemonListUseCase

class PokemonViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PokemonState())
    val state: StateFlow<PokemonState> = _state.asStateFlow()

    fun handleIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemonList -> viewModelScope.launch {
                loadPokemonList()
            }
        }
    }

    private suspend fun loadPokemonList() {
        _state.value = PokemonState(isLoading = true)
        val pokemonList = getPokemonListUseCase.execute()
        _state.value = PokemonState(
            pokemonUIList = pokemonList.map { pokemon -> PokemonUI.from(pokemon) },
            isLoading = false
        )
    }
}