package di

import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.Job
import org.koin.core.module.Module
import org.koin.dsl.module
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.repository.PokemonRepository
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.domain.usecase.GetPokemonListSortedUseCase
import pokemon.presentation.PokemonViewModel

// Pour exposer les types d'interop nécessaires à Swift
typealias SharedKotlinx_coroutines_coreFlowCollector<T> = FlowCollector<T>
typealias Kotlinx_coroutines_coreJob = Job
/**
 * Configuration de l'injection de dépendances avec Koin
 * https://insert-koin.io/docs/reference/koin-core/definitions/
 * ✅ single() : Pour les composants avec état (ViewModel avec StateFlow)
 * ✅ factory() : Pour les composants stateless (DataSource, UseCase, Repository)
 * 
 * Avantages :
 * - ViewModel : Instance unique pour éviter les problèmes de synchronisation d'état
 * - Autres composants : Nouvelle instance à chaque injection pour optimiser la mémoire
 */
val commonModule: Module = module {
    // Repository : stateless → factory
    factory<PokemonRepository> { PokemonRepositoryImpl(get()) }
    
    // DataSource : stateless → factory  
    factory { PokemonRemoteDataSource() }
    
    // UseCases : stateless → factory
    factory { GetPokemonListUseCase(get()) }
    factory { GetPokemonListSortedUseCase(get()) }
    
    // ViewModel : avec état (StateFlow) → single pour éviter les problèmes de synchronisation
    single { PokemonViewModel(get(), get()) }
}
