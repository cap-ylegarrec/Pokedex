package di

import core.ItemDriverFactory
import core.PokeDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val iOSCoreModule: Module = module {
    includes(coreModule)
    single { ItemDriverFactory() }
    single { PokeDriverFactory() }
}