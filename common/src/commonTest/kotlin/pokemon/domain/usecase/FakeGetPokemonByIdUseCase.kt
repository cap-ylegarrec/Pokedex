package pokemon.domain.usecase

import pokemon.data.repositoryimpl.FakePokemonRepositoryImpl
import pokemon.domain.model.Pokemon

class FakeGetPokemonByIdUseCase : GetPokemonByIdUseCase(FakePokemonRepositoryImpl()) {
    private val pokemonData = mutableMapOf<Long, Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonData[pokemon.id] = pokemon
    }

    override suspend fun execute(id: Long): Pokemon? {
        return pokemonData[id]
    }
}