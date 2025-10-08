package pokemon.data.datasource

import com.pokedex.pokemon.PokemonDatabase
import kotlinx.serialization.json.Json
import pokemon.domain.model.Pokemon

open class PokemonLocalDataSource(private val database: PokemonDatabase) {
    private val queries = database.pokemonQueries

    open fun getPokemonList(): List<Pokemon> {
        return queries.selectAll().executeAsList().map { entity ->
            Pokemon(
                id = entity.id,
                pokedexId = entity.pokedexId,
                name = entity.name,
                typeList = Json.decodeFromString(entity.typeList),
                imageUrl = entity.imageUrl,
                sprite = entity.sprite,
                generation = entity.generation.toInt(),
                slug = entity.slug,
                stats = Json.decodeFromString(entity.stats),
                preEvolution = entity.preEvolution?.let { Json.decodeFromString(it) },
                evolutions = Json.decodeFromString(entity.evolutions),
                resistances = Json.decodeFromString(entity.resistances)
            )
        }
    }

    open fun getPokemonById(id: Long): Pokemon? {
        return queries.selectById(id).executeAsOneOrNull()?.let { entity ->
            Pokemon(
                id = entity.id,
                pokedexId = entity.pokedexId,
                name = entity.name,
                typeList = Json.decodeFromString(entity.typeList),
                imageUrl = entity.imageUrl,
                sprite = entity.sprite,
                generation = entity.generation.toInt(),
                slug = entity.slug,
                stats = Json.decodeFromString(entity.stats),
                preEvolution = entity.preEvolution?.let { Json.decodeFromString(it) },
                evolutions = Json.decodeFromString(entity.evolutions),
                resistances = Json.decodeFromString(entity.resistances)
            )
        }
    }

    open fun insertPokemonList(pokemonList: List<Pokemon>) {
        pokemonList.forEach { pokemon ->
            queries.insertOrReplace(
                id = pokemon.id,
                pokedexId = pokemon.pokedexId,
                name = pokemon.name,
                typeList = Json.encodeToString(pokemon.typeList),
                imageUrl = pokemon.imageUrl,
                sprite = pokemon.sprite,
                generation = pokemon.generation.toLong(),
                slug = pokemon.slug,
                stats = Json.encodeToString(pokemon.stats),
                preEvolution = pokemon.preEvolution?.let { Json.encodeToString(it) },
                evolutions = Json.encodeToString(pokemon.evolutions),
                resistances = Json.encodeToString(pokemon.resistances)
            )
        }
    }

    fun clearPokemonListFromCache() {
        queries.deleteAll()
    }
}