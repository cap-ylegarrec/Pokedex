package com.legarrec.pokedex

import android.app.Application
import com.legarrec.pokedex.di.appModule
import di.androidCommonModule
import di.androidCoreModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PokedexApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PokedexApplication)
            modules(androidCommonModule, androidCoreModule, appModule)
        }
    }
}
