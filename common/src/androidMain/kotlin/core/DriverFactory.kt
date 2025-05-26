package core

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import pokemon.data.datasource.PokemonDB

actual class DriverFactory(private val context: Context) {
    actual fun createDriver(databaseName: String): SqlDriver {
        return AndroidSqliteDriver(PokemonDB.Schema, context, "PokemonDB")
    }
}