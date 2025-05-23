package pokemon.domain.repository

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import pokemon.domain.model.Pokemon

interface PokemonRepository {
    @NativeCoroutines
    suspend fun getPokemonList(): List<Pokemon>
}