package pokemon.data.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import pokemon.domain.model.Evolution
import pokemon.domain.model.Pokemon
import pokemon.domain.model.PreEvolution
import pokemon.domain.model.Resistance
import pokemon.domain.model.Stats
import pokemon.domain.model.Type

@Serializable
data class ApiStats(
    val HP: Int,
    val attack: Int,
    val defense: Int,
    @SerialName("special_attack")
    val specialAttack: Int,
    @SerialName("special_defense")
    val specialDefense: Int,
    val speed: Int
)

@Serializable
data class ApiPokemon(
    val id: Long,
    @SerialName("pokedexId")
    val pokedexId: Long,
    val name: String,
    val image: String,
    val sprite: String,
    @SerialName("apiGeneration")
    val generation: Int,
    val slug: String,
    val stats: ApiStats,
    @SerialName("apiTypes")
    val typeList: List<ApiType>,
    @SerialName("apiPreEvolution")
    val preEvolution: ApiPreEvolution?,
    @SerialName("apiEvolutions")
    val evolutions: List<ApiEvolution>,
    val resistances: List<ApiResistance> = emptyList() // Default to an empty list
)

@Serializable
data class ApiType(
    val name: String,
    val image: String
)

@Serializable(with = ApiPreEvolutionSerializer::class)
sealed class ApiPreEvolution {
    @Serializable
    data class PreEvolutionData(
        val name: String,
        val pokedexIdd: Long
    ) : ApiPreEvolution()

    @Serializable
    object None : ApiPreEvolution()
}

object ApiPreEvolutionSerializer : KSerializer<ApiPreEvolution> {
    override val descriptor: SerialDescriptor = JsonElement.serializer().descriptor

    override fun deserialize(decoder: Decoder): ApiPreEvolution {
        val element = decoder.decodeSerializableValue(JsonElement.serializer())
        return if (element is JsonPrimitive && element.content == "none") {
            ApiPreEvolution.None
        } else {
            Json.decodeFromJsonElement(ApiPreEvolution.PreEvolutionData.serializer(), element)
        }
    }

    override fun serialize(encoder: Encoder, value: ApiPreEvolution) {
        when (value) {
            is ApiPreEvolution.None -> encoder.encodeString("none")
            is ApiPreEvolution.PreEvolutionData -> encoder.encodeSerializableValue(
                ApiPreEvolution.PreEvolutionData.serializer(),
                value
            )
        }
    }
}

@Serializable
data class ApiEvolution(
    val name: String,
    val pokedexId: Long
)

@Serializable
data class ApiResistance(
    val name: String,
    val damageMultiplier: Double,
    val damageRelation: String
)

open class PokemonRemoteDataSource {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }


    open suspend fun getPokemonList(generation: Int): List<Pokemon> = withContext(Dispatchers.IO) {
        val response: List<ApiPokemon> =
            client.get("https://pokebuildapi.fr/api/v1/pokemon/generation/$generation") {
                contentType(ContentType.Application.Json)
            }.body()

        response.map { apiPokemon ->
            Pokemon(
                id = apiPokemon.id,
                pokedexId = apiPokemon.pokedexId,
                name = apiPokemon.name,
                typeList = apiPokemon.typeList.map { apiType ->
                    Type(
                        apiType.name,
                        apiType.image
                    )
                },
                imageUrl = apiPokemon.image,
                sprite = apiPokemon.sprite,
                generation = apiPokemon.generation,
                slug = apiPokemon.slug,
                stats = Stats(
                    HP = apiPokemon.stats.HP,
                    attack = apiPokemon.stats.attack,
                    defense = apiPokemon.stats.defense,
                    specialAttack = apiPokemon.stats.specialAttack,
                    specialDefense = apiPokemon.stats.specialDefense,
                    speed = apiPokemon.stats.speed
                ),
                preEvolution = (apiPokemon.preEvolution as? ApiPreEvolution.PreEvolutionData)?.let {
                    PreEvolution(it.name, it.pokedexIdd)
                },
                evolutions = apiPokemon.evolutions.map { evolution ->
                    Evolution(evolution.name, evolution.pokedexId)
                },
                resistances = apiPokemon.resistances.map { resistance ->
                    Resistance(
                        name = resistance.name,
                        damageMultiplier = resistance.damageMultiplier,
                        damageRelation = resistance.damageRelation
                    )
                }
            )
        }
    }
}