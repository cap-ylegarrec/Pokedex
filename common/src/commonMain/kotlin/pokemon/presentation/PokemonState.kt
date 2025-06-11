package pokemon.presentation

data class PokemonState(
    val pokemonUIList: List<PokemonUI> = emptyList(), 
    val isLoading: Boolean = false,
    val isSortedAlphabetically: Boolean = false
)
