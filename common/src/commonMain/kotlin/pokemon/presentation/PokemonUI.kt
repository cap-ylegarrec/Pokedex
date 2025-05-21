package pokemon.presentation

import pokemon.domain.model.Pokemon

data class PokemonUI(val id: Int, val name: String, val colorList: List<String>, val imageUrl: String) {

    //Create PokemonUI from Pokemon
    companion object {
        fun from(pokemon: Pokemon): PokemonUI {
            return PokemonUI(
                id = pokemon.id,
                name = pokemon.name,
                colorList = pokemon.typeList.mapNotNull { type -> getColorByType(type) },
                imageUrl = pokemon.imageUrl
            )
        }

        //TODO manage all the types
        // return color in hexadecimal format from type
        private fun getColorByType(type: String): String? {
            return when (type) {
                "Feu" -> "#FF4500"
                "Eau" -> "#1E90FF"
                "Plante" -> "#32CD32"
                "Électrik" -> "#FFD700"
                "Roche" -> "#8B4513"
                "Sol" -> "#D2691E"
                "Psy" -> "#FF69B4"
                "Insecte" -> "#ADFF2F"
                "Normal" -> "#A9A9A9"
                "Vol" -> "#87CEEB"
                "Spectre" -> "#800080"
                "Acier" -> "#C0C0C0"
                "Fée" -> "#FFB6C1"
                "Combat" -> "#FF6347"
                "Poison" -> "#8A2BE2"
                "Ténèbres" -> "#000000"
                "Dragon" -> "#0000FF"
                "Glace" -> "#00FFFF"
                else -> null // Default color
            }
        }
    }
}
