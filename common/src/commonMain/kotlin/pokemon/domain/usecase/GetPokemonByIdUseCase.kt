package pokemon.domain.usecase

import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class GetPokemonByIdUseCase(
    private val repository: PokemonRepository
) {
    suspend fun execute(id: Long): Pokemon? {
        return repository.getPokemonById(id)
    }
}
