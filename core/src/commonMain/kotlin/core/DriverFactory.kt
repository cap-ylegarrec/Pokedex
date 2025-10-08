package core

import app.cash.sqldelight.db.SqlDriver

expect class ItemDriverFactory {
    fun createDriver(databaseName: String): SqlDriver
}

expect class PokeDriverFactory {
    fun createDriver(databaseName: String): SqlDriver
}