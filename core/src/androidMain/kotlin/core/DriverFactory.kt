package core

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pokedex.item.ItemDatabase
import com.pokedex.pokemon.PokemonDatabase

actual class ItemDriverFactory(private val context: Context) {
    actual fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(ItemDatabase.Schema, context, databaseName)
}

actual class PokeDriverFactory(private val context: Context) {
    actual fun createDriver(databaseName: String): SqlDriver =
        AndroidSqliteDriver(PokemonDatabase.Schema, context, databaseName)
}