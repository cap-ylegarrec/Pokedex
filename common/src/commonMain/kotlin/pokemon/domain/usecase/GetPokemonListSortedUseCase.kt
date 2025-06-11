package pokemon.domain.usecase

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class GetPokemonListSortedUseCase(
    private val repository: PokemonRepository
) {
    @NativeCoroutines
    suspend fun execute(): List<Pokemon> {
        return repository.getPokemonList().sortedBy { it.name }
    }
} 