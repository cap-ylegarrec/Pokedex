package di

import core.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val androidCommonModule: Module = module {
    includes(commonModule)
    single { DriverFactory(get()) }
}