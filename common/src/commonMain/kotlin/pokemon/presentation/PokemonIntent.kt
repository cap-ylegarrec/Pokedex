package pokemon.presentation

sealed class PokemonIntent {
    data object LoadPokemonList : PokemonIntent()
}

