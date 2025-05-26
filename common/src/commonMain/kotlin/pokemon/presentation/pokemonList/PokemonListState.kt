package pokemon.presentation.pokemonList

import pokemon.presentation.PokemonUI

data class PokemonListState(val pokemonUIList: List<PokemonUI> = emptyList(), val isLoading: Boolean = false)
