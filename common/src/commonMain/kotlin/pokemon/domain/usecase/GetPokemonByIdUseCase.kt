package pokemon.domain.usecase

import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

open class GetPokemonByIdUseCase(
    private val repository: PokemonRepository
) {
    open suspend fun execute(id: Long): Pokemon? {
        return repository.getPokemonById(id)
    }
}
