package pokemon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Pokemon(
    val id: Long,
    val pokedexId: Long,
    val name: String,
    val typeList: List<Type>,
    val imageUrl: String,
    val sprite: String,
    val generation: Int,
    val slug: String,
    val stats: Stats,
    val evolutions: List<Evolution>,
    val preEvolution: PreEvolution?,
    val resistances: List<Resistance>
)

@Serializable
data class Evolution(
    val name: String,
    val pokedexId: Long
)

@Serializable
data class PreEvolution(
    val name: String,
    val pokedexId: Long
)

@Serializable
data class Stats(
    val HP: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
)

@Serializable
data class Resistance(
    val name: String,
    val damageMultiplier: Double,
    val damageRelation: String
)