package core

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.pokedex.item.ItemDatabase
import com.pokedex.poke.PokemonDatabase

actual class ItemDriverFactory {
    actual fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(ItemDatabase.Schema, databaseName)
}

actual class PokeDriverFactory {
    actual fun createDriver(databaseName: String): SqlDriver =
        NativeSqliteDriver(PokemonDatabase.Schema, databaseName)
}