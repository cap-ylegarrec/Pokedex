package di

import core.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module
import pokemon.data.datasource.PokemonDB
import pokemon.data.datasource.PokemonLocalDataSource
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.repository.PokemonRepository
import pokemon.domain.usecase.GetPokemonByIdUseCase
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.presentation.pokemonDetail.PokemonViewModel
import pokemon.presentation.pokemonList.PokemonListViewModel

val commonModule: Module = module {

    // ViewModel
    single { PokemonListViewModel(get()) }
    factory { PokemonViewModel(get()) }

    //Use Cases
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonByIdUseCase(get()) }

    //Repository
    factory<PokemonRepository> { PokemonRepositoryImpl(get(), get()) }

    //Data Source
    factory { PokemonRemoteDataSource() }
    factory { PokemonLocalDataSource(get()) }

    //Database
    single {
        val driverFactory: DriverFactory = get()
        PokemonDB(
            driver = driverFactory.createDriver("PokemonDB")
        )
    }
}