package core

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import pokemon.data.datasource.PokemonDB

actual class DriverFactory {
    actual fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(PokemonDB.Schema, databaseName)
}