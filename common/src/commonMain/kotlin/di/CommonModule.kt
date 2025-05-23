package di

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.Job
import org.koin.core.module.Module
import org.koin.dsl.module
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.repository.PokemonRepository
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.presentation.PokemonViewModel

// Pour exposer les types d'interop nécessaires à Swift
typealias SharedKotlinx_coroutines_coreFlowCollector<T> = FlowCollector<T>
typealias Kotlinx_coroutines_coreJob = Job

val commonModule: Module = module {
    factory { PokemonRemoteDataSource() }
    factory<PokemonRepository> { PokemonRepositoryImpl(get()) }
    factory { GetPokemonListUseCase(get()) }
    factory { PokemonViewModel(get()) }
}