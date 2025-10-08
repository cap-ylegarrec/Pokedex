package di

import org.koin.core.module.Module
import org.koin.dsl.module

val iOSCoreModule: Module = module {
    includes(coreModule)
}