package pokemon.presentation.pokemonDetail

sealed class PokemonIntent {
    data class LoadPokemonById(val id: Long) : PokemonIntent()
}

