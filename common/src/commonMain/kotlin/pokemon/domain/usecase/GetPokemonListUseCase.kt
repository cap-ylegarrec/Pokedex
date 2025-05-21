package pokemon.domain.usecase

import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    suspend fun execute(): List<Pokemon> {
        return repository.getPokemonList()
    }
}
