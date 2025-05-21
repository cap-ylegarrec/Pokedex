package pokemon.domain.repository

import pokemon.domain.model.Pokemon

interface PokemonRepository {
    suspend fun getPokemonList(): List<Pokemon>
}