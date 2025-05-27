package pokemon.presentation.pokemonList

sealed class PokemonListIntent {
    data object LoadPokemonList : PokemonListIntent()
    data object LoadNextGeneration : PokemonListIntent()
    data class SearchPokemon(val searched: String) : PokemonListIntent()
}

