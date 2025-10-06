package pokemon.domain.usecase

import pokemon.data.repositoryimpl.FakePokemonRepositoryImpl
import pokemon.domain.model.Pokemon

class FakeGetPokemonListUseCase : GetPokemonListUseCase(FakePokemonRepositoryImpl()) {
    private val pokemonData = mutableListOf<Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonData.add(pokemon)
    }

    override suspend fun execute(generation: Int): List<Pokemon> {
        return pokemonData.filter { it.generation == generation }
    }
}