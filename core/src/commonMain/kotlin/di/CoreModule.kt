package di

import com.pokedex.item.ItemDatabase
import com.pokedex.pokemon.PokemonDatabase
import core.ItemDriverFactory
import core.PokeDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

val coreModule: Module = module {

    // ViewModel

    //Use Cases

    //Repository

    //Data Source

    //Database
    single { ItemDatabase(get<ItemDriverFactory>().createDriver("ItemDatabase")) }
    single { PokemonDatabase(get<PokeDriverFactory>().createDriver("PokeDatabase")) }
}