package pokemon.domain.usecase

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

open class GetPokemonListUseCase(
    private val repository: PokemonRepository
) {
    @NativeCoroutines
    open suspend fun execute(generation: Int): List<Pokemon> {
        return repository.getPokemonListByGeneration(generation)
    }
}
