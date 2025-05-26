import di.commonModule

import core.DriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val iOSCommonModule: Module = module {
    includes(commonModule)
    single { DriverFactory() }
}