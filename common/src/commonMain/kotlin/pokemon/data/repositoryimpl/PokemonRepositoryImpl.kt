package pokemon.data.repositoryimpl

import pokemon.data.datasource.PokemonLocalDataSource
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val pokemonRemoteDataSource: PokemonRemoteDataSource,
    private val pokemonLocalDataSource: PokemonLocalDataSource
) : PokemonRepository {

    override suspend fun getPokemonListByGeneration(generation: Int): List<Pokemon> {
        val cached = pokemonLocalDataSource.getPokemonList().filter { it.generation == generation }
        return if (cached.isNotEmpty()) {
            cached
        } else {
            val remote = pokemonRemoteDataSource.getPokemonList(generation)
            if (remote.isNotEmpty()) {
                pokemonLocalDataSource.insertPokemonList(remote)
            }
            remote
        }
    }

    override suspend fun getPokemonById(id: Long): Pokemon? {
        return pokemonLocalDataSource.getPokemonById(id)
    }

    override suspend fun clearPokemonListFromCache() {
        pokemonLocalDataSource.clearPokemonListFromCache()
    }
}