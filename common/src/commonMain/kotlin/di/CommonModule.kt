package di

import org.koin.core.module.Module
import org.koin.dsl.module
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.repository.PokemonRepository
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.presentation.PokemonViewModel

val commonModule: Module = module {
    factory { PokemonRemoteDataSource() }
    factory<PokemonRepository> { PokemonRepositoryImpl(get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { PokemonViewModel(get()) }
}