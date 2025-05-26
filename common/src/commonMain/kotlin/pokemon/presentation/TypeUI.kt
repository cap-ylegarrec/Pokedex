package pokemon.presentation

import pokemon.domain.model.Type
import pokemon.presentation.config.TypeColors

data class TypeUI(val name: String, val color: String?, val iconUrl: String) {

    //Create PokemonUI from Pokemon
    companion object {
        fun from(type: Type): TypeUI {
            return TypeUI(
                name = type.name,
                color = getColorByType(type.name),
                iconUrl = type.imageUrl
            )
        }

        private fun getColorByType(typeName: String): String? {
            return TypeColors.colors[typeName]
        }
    }
}
