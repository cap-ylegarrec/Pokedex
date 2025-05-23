package pokemon.data.datasource

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pokemon.domain.model.Pokemon
import com.rickclephas.kmp.nativecoroutines.NativeCoroutines

@Serializable
data class ApiPokemon(
    val id: Int,
    val name: String,
    val image: String,
    @SerialName("apiTypes")
    val typeList: List<ApiType>
)

@Serializable
data class ApiType(
    val name: String
)

class PokemonRemoteDataSource {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    @NativeCoroutines
    suspend fun getPokemonList(): List<Pokemon> = withContext(Dispatchers.Default) {
        val response: List<ApiPokemon> =
            client.get("https://pokebuildapi.fr/api/v1/pokemon/limit/151") {
                contentType(ContentType.Application.Json)
            }.body()

        response.map { apiPokemon ->
            Pokemon(
                id = apiPokemon.id,
                name = apiPokemon.name,
                typeList = apiPokemon.typeList.map { apiType -> apiType.name },
                imageUrl = apiPokemon.image
            )
        }
    }
}