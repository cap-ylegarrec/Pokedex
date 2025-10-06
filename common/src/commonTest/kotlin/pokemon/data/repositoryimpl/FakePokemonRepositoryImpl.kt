package pokemon.data.repositoryimpl

import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class FakePokemonRepositoryImpl : PokemonRepository {
    private val pokemonData = mutableMapOf<Long, Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonData[pokemon.id] = pokemon
    }

    override suspend fun getPokemonListByGeneration(generation: Int): List<Pokemon> {
        return pokemonData.values.toList()
    }

    override suspend fun getPokemonById(id: Long): Pokemon? {
        return pokemonData[id]
    }

    override suspend fun clearPokemonListFromCache() {

    }
}