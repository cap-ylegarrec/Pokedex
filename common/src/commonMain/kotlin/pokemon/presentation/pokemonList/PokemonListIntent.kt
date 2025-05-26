package pokemon.presentation.pokemonList

sealed class PokemonListIntent {
    data object LoadPokemonList : PokemonListIntent()
}

