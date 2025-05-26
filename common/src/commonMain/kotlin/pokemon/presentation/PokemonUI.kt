package pokemon.presentation

import kotlinx.serialization.Serializable
import pokemon.domain.model.Pokemon
import pokemon.domain.model.Stats
import pokemon.presentation.config.TypeColors

data class PokemonUI(
    val id: Long,
    val name: String,
    val backgroundColorList: List<String>,
    val typeList: List<TypeUI>,
    val imageUrl: String,
    val spriteUrl: String,
    var previousEvolution: List<PokemonUI?> = emptyList(),
    var nextEvolution: List<PokemonUI> = emptyList(),
    var statsUI: StatsUI
) {

    //Create PokemonUI from Pokemon
    companion object {
        fun from(pokemon: Pokemon): PokemonUI {
            return PokemonUI(
                id = pokemon.id,
                name = pokemon.name,
                backgroundColorList = pokemon.typeList.mapNotNull { type ->
                    getBackgroundColorByType(
                        type.name
                    )
                },
                typeList = pokemon.typeList.map { type -> TypeUI.from(type) },
                imageUrl = pokemon.imageUrl,
                spriteUrl = pokemon.sprite,
                statsUI = StatsUI.from(pokemon.stats)
            )
        }

        private fun getBackgroundColorByType(typeName: String): String? {
            return TypeColors.colors[typeName]
        }
    }
}

@Serializable
data class StatsUI(
    val HP: Int,
    val attack: Int,
    val defense: Int,
    val specialAttack: Int,
    val specialDefense: Int,
    val speed: Int
) {
    companion object {
        fun from(stats: Stats): StatsUI {
            return StatsUI(
                HP = stats.HP,
                attack = stats.attack,
                defense = stats.defense,
                specialAttack = stats.specialAttack,
                specialDefense = stats.specialDefense,
                speed = stats.speed
            )
        }
    }
}