package pokemon.presentation

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import core.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pokemon.domain.usecase.GetPokemonListUseCase

class PokemonViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase
) : ViewModel() {

    private val _uiState: MutableStateFlow<PokemonState> = MutableStateFlow(PokemonState(isLoading = true))

    @NativeCoroutinesState
    val uiState: StateFlow<PokemonState> = _uiState.asStateFlow()

    fun handleIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemonList -> viewModelScope.launch {
                loadPokemonList()
            }
        }
    }

    private suspend fun loadPokemonList() {
        _uiState.value = PokemonState(isLoading = true)
        val pokemonList = getPokemonListUseCase.execute()
        _uiState.value = PokemonState(
            pokemonUIList = pokemonList.map { pokemon -> PokemonUI.from(pokemon) },
            isLoading = false
        )
    }
}