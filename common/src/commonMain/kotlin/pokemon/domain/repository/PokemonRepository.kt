package pokemon.domain.repository

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import pokemon.domain.model.Pokemon

interface PokemonRepository {

    @NativeCoroutines
    suspend fun getPokemonListByGeneration(generation: Int): List<Pokemon>

    @NativeCoroutines
    suspend fun getPokemonById(id: Long): Pokemon?

    @NativeCoroutines
    suspend fun clearPokemonListFromCache()
}