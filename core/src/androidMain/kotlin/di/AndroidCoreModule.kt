package di

import core.ItemDriverFactory
import core.PokeDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val androidCoreModule: Module = module {
    includes(coreModule)
    single { ItemDriverFactory(get()) }
    single { PokeDriverFactory(get()) }
}