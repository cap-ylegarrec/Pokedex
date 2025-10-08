package pokemon.data.datasource

import app.cash.sqldelight.TransactionWithReturn
import app.cash.sqldelight.TransactionWithoutReturn
import com.pokedex.pokemon.PokemonDatabase
import com.pokedex.pokemon.PokemonQueries
import pokemon.domain.model.Pokemon

class FakePokemonLocalDataSource : PokemonLocalDataSource(FakePokemonDb()) {
    private val pokemonData = mutableListOf<Pokemon>()

    fun addPokemon(pokemon: Pokemon) {
        pokemonData.add(pokemon)
    }

    override fun getPokemonList(): List<Pokemon> {
        return pokemonData
    }

    override fun getPokemonById(id: Long): Pokemon? {
        return pokemonData.find { it.id == id }
    }

    override fun insertPokemonList(pokemonList: List<Pokemon>) {
        pokemonData.addAll(pokemonList)
    }
}

class FakePokemonDb() : PokemonDatabase {
    override lateinit var pokemonQueries: PokemonQueries

    override fun transaction(
        noEnclosing: Boolean,
        body: TransactionWithoutReturn.() -> Unit
    ) {
        throw NotImplementedError("No need to implement for fake database")
    }

    override fun <R> transactionWithResult(
        noEnclosing: Boolean,
        bodyWithReturn: TransactionWithReturn<R>.() -> R
    ): R {
        throw NotImplementedError("No need to implement for fake database")
    }

}