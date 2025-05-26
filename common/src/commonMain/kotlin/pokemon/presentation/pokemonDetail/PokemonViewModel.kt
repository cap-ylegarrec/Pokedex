package pokemon.presentation.pokemonDetail

import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import core.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import pokemon.domain.model.Evolution
import pokemon.domain.model.PreEvolution
import pokemon.domain.usecase.GetPokemonByIdUseCase
import pokemon.presentation.PokemonUI
import pokemon.presentation.pokemonList.PokemonListIntent
import pokemon.presentation.pokemonList.PokemonListState

class PokemonViewModel(
    private val getPokemonByIdUseCase: GetPokemonByIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(PokemonState(null, isLoading = true))

    @NativeCoroutinesState
    val state: StateFlow<PokemonState> = _state.asStateFlow()

    fun handleIntent(intent: PokemonIntent) {
        when (intent) {
            is PokemonIntent.LoadPokemonById -> viewModelScope.launch {
                loadPokemonById(intent.id)
            }
        }
    }

    private suspend fun loadPokemonById(id: Long) {
        _state.value = PokemonState(pokemonUI = null, isLoading = true)
        val pokemon = getPokemonByIdUseCase.execute(id)
        val pokemonUI = pokemon?.let {
            val pokemonUI = PokemonUI.from(it)

            if (pokemon.preEvolution?.pokedexId != null) {
                pokemonUI.previousEvolution = getAllPreEvolutionsRecursively(pokemon.preEvolution)
            }

            if (pokemon.evolutions.isNotEmpty()) {
                pokemonUI.nextEvolution = getAllEvolutionsRecursively(pokemon.evolutions)
            }

            pokemonUI
        }
        _state.value = PokemonState(
            pokemonUI = pokemonUI,
            isLoading = false
        )
    }

    private suspend fun getAllPreEvolutionsRecursively(preEvolution: PreEvolution): List<PokemonUI> {
        val preEvolutionList = mutableListOf<PokemonUI>()
        preEvolution.let { preEvolution ->
            getPokemonByIdUseCase.execute(preEvolution.pokedexId)?.let { preEvolutionPokemon ->
                val evolutionUI = PokemonUI.from(preEvolutionPokemon)
                if (preEvolutionPokemon.preEvolution != null) {
                    val tempPreEvolutionList =
                        getAllPreEvolutionsRecursively(preEvolutionPokemon.preEvolution)
                    evolutionUI.previousEvolution = tempPreEvolutionList
                    preEvolutionList.addAll(tempPreEvolutionList)
                }
                preEvolutionList.add(evolutionUI)
            }
        }
        return preEvolutionList.sortedBy { it.id }
    }

    private suspend fun getAllEvolutionsRecursively(evolutions: List<Evolution>): List<PokemonUI> {
        val evolutionList = mutableListOf<PokemonUI>()
        evolutions.mapNotNull { evolution ->
            getPokemonByIdUseCase.execute(evolution.pokedexId)?.let { evolutionPokemon ->
                val evolutionUI = PokemonUI.from(evolutionPokemon)
                if (evolutionPokemon.evolutions.isNotEmpty()) {
                    val tempEvolutionList = getAllEvolutionsRecursively(evolutionPokemon.evolutions)
                    evolutionUI.nextEvolution = tempEvolutionList
                    evolutionList.addAll(tempEvolutionList)
                }
                if (evolutionPokemon.preEvolution != null){
                    val tempPreEvolutionList =
                        getAllPreEvolutionsRecursively(evolutionPokemon.preEvolution)
                    evolutionUI.previousEvolution = tempPreEvolutionList
                }
                evolutionList.add(evolutionUI)
            }
        }
        return evolutionList.sortedBy { it.id }
    }

}