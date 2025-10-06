package pokemon.data.datasource

import pokemon.domain.model.Pokemon

class FakePokemonRemoteDataSource : PokemonRemoteDataSource() {
    private val pokemonData = mutableListOf<Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonData.add(pokemon)
    }

    override suspend fun getPokemonList(generation: Int): List<Pokemon> {
        return pokemonData.filter { it.generation == generation }
    }
}