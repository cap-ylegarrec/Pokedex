package pokemon.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Type(val name: String, val imageUrl: String)