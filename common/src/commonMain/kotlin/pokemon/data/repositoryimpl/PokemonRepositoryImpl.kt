package pokemon.data.repositoryimpl

import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class PokemonRepositoryImpl(private val pokemonRemoteDataSource: PokemonRemoteDataSource) : PokemonRepository {
    override suspend fun getPokemonList(): List<Pokemon> {
        return pokemonRemoteDataSource.getPokemonList()
    }
}