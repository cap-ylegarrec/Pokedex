package com.legarrec.pokedex.pokemon

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import coil3.compose.AsyncImage
import com.legarrec.pokedex.R
import com.legarrec.pokedex.ui.PokedexColors
import com.legarrec.pokedex.ui.PokedexDimens
import com.legarrec.pokedex.ui.backgroundWithColors
import org.koin.compose.koinInject
import pokemon.presentation.PokemonUI
import pokemon.presentation.StatsUI
import pokemon.presentation.TypeUI
import pokemon.presentation.pokemonDetail.PokemonIntent
import pokemon.presentation.pokemonDetail.PokemonViewModel
import java.util.Locale

@Composable
fun PokemonView(
    pokemonId: Long, onBack: () -> Unit,
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: ((String) -> Unit)? = null,
    selectedTab: String?
) {
    val viewModel: PokemonViewModel = koinInject()
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.handleIntent(PokemonIntent.LoadPokemonById(pokemonId))
    }

    // Handle the loaded Pokemon data
    if (state.isLoading == true) {
        // Show loading indicator
        Text(text = "Loading...", style = MaterialTheme.typography.h6)
        return
    } else if (state.pokemonUI == null) {
        Text(text = "Pokemon not found", style = MaterialTheme.typography.h6)
        return
    } else {
        PokemonDetailScreen(state.pokemonUI!!, onBack, onItemClick, onSpeak, selectedTab ?: "Info")
    }
}

@Composable
fun PokemonDetailScreen(
    pokemon: PokemonUI, onBack: () -> Unit,
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: ((String) -> Unit)?,
    defaultSelectedTab: String = "Info"
) {
    val scrollState = rememberScrollState()
    var selectedTab by remember { mutableStateOf(defaultSelectedTab) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .backgroundWithColors(pokemon.backgroundColorList)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState) // Ensure the view is scrollable

        ) {
            // Header with gradient and Pokémon info
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(PokedexDimens.PokemonHeaderHeight)
                    .padding(top = 80.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(PokedexDimens.TopBarPadding),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back_arrow),
                        contentDescription = null,
                        tint = PokedexColors.White,
                        modifier = Modifier.clickable { onBack() }
                    )
                    IconButton(
                        modifier = Modifier.size(40.dp),
                        onClick = {
                            onSpeak?.invoke(
                                "${pokemon.name} est de type ${
                                    pokemon.typeList.joinToString(
                                        separator = " et "
                                    ) { it.name }
                                }")
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.sound),
                            contentDescription = "Speak",
                            tint = PokedexColors.White
                        )
                    }
                }
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(
                            horizontal = PokedexDimens.PokemonListHeaderPaddingHorizontal,
                            vertical = PokedexDimens.TopBarPadding
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        pokemon.name,
                        fontSize = PokedexDimens.PokemonNameFont,
                        fontWeight = FontWeight.Bold,
                        color = PokedexColors.White
                    )
                    Text(
                        String.format(Locale.getDefault(), "#%03d", pokemon.id),
                        color = PokedexColors.White,
                        fontSize = PokedexDimens.PokemonIdFont
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(PokedexDimens.PokemonListTypeRowSpacing),
                        modifier = Modifier.padding(top = PokedexDimens.PokemonListTypeRowPaddingTop)
                    ) {
                        pokemon.typeList.forEach { type ->
                            TypeBadge(
                                type.name,
                                Color((type.color ?: "0xFF71CE7E").toColorInt())
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(PokedexDimens.PokemonListSpacerHeight))
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        placeholder = painterResource(R.drawable.pokeball),
                        error = painterResource(R.drawable.honorball),
                        modifier = Modifier
                            .size(PokedexDimens.PokemonImageSize * 2)
                            .align(Alignment.CenterHorizontally),
                        contentScale = ContentScale.Fit,
                        onError = { e -> Log.d("PokemonView", "Error loading image: ${e.result}") }
                    )
                }
            }

            ContentArea(
                pokemon,
                onItemClick,
                onSpeak,
                selectedTab,
                onSelectedTabChange = { newTab ->
                    selectedTab = newTab
                }
            ) // Content area with tabs and details
            // Fill remaining space with details area background color
            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f, fill = true)
                    .background(PokedexColors.Background)
            )
        }
    }
}

// Updated TypeBadge to add a 3D elevation effect
@Composable
fun TypeBadge(name: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color, RoundedCornerShape(50))
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(50))
            .padding(
                horizontal = PokedexDimens.TypeBadgeHorizontal,
                vertical = PokedexDimens.TypeBadgeVertical
            )
    ) {
        Text(name, color = PokedexColors.White, fontSize = PokedexDimens.TypeBadgeFont)
    }
}

@Composable
fun TabItem(title: String, isSelected: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) PokedexColors.White else PokedexColors.Gray,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(PokedexDimens.TabIndicatorHeight)
                    .width(PokedexDimens.TabIndicatorWidth)
                    .background(
                        PokedexColors.GreenGradientStart,
                        RoundedCornerShape(PokedexDimens.TabIndicatorCorner)
                    )
            )
        }
    }
}

@Composable
fun StatRow(label: String, value: Int, max: Int, color: Color) {
    Column(modifier = Modifier.padding(vertical = PokedexDimens.StatRowVertical)) {
        Text(
            "$label ${String.format(Locale.getDefault(), "%03d", value)}",
            color = PokedexColors.White
        )
        LinearProgressIndicator(
            progress = value.toFloat() / max.toFloat(),
            color = color,
            backgroundColor = PokedexColors.StatBarBackground,
            modifier = Modifier
                .fillMaxWidth()
                .height(PokedexDimens.StatBarHeight)
                .clip(RoundedCornerShape(PokedexDimens.StatBarCorner))
        )
    }
}

@Composable
fun StatArea(pokemon: PokemonUI) {
    // Details area for Info tab
    StatRow("HP", pokemon.statsUI.HP, 255, PokedexColors.StatBar)
    StatRow("ATK", pokemon.statsUI.attack, 255, PokedexColors.StatBar)
    StatRow("DEF", pokemon.statsUI.defense, 255, PokedexColors.StatBar)
    StatRow("SATK", pokemon.statsUI.specialAttack, 255, PokedexColors.StatBar)
    StatRow("SDEF", pokemon.statsUI.specialDefense, 255, PokedexColors.StatBar)
    StatRow("SPD", pokemon.statsUI.speed, 255, PokedexColors.StatBar)
}

@Composable
fun EvolutionItem(
    pokemon: PokemonUI?,
    showChevron: Boolean,
    onClick: (PokemonUI) -> Unit,
    onSpeak: ((String) -> Unit)?
) {
    if (pokemon != null) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(pokemon) },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            PokemonViewItem(pokemon = pokemon, onSpeak = onSpeak, onClick = onClick)
            if (showChevron) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_chevron_down),
                    contentDescription = "Chevron Down",
                    tint = PokedexColors.White,
                    modifier = Modifier.padding(16.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(32.dp)) // Add space if no chevron
            }
        }
    }
}


@Composable
fun EvolutionArea(
    pokemon: PokemonUI,
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: ((String) -> Unit)?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        pokemon.previousEvolution.forEach {
            EvolutionItem(
                pokemon = it,
                showChevron = true,
                onClick = onItemClick,
                onSpeak = onSpeak
            )
        }
        EvolutionItem(
            pokemon = pokemon,
            showChevron = pokemon.nextEvolution.isNotEmpty(),
            onClick = {},
            onSpeak = onSpeak
        )
        pokemon.nextEvolution.forEachIndexed { index, evolution ->
            val showChevron = if (index < pokemon.nextEvolution.count() - 1) {
                val nextPokemon = pokemon.nextEvolution[index + 1]
                nextPokemon.previousEvolution.any { it!!.id == evolution.id }
            } else {
                false
            }

            EvolutionItem(
                pokemon = evolution,
                showChevron = showChevron,
                onClick = onItemClick,
                onSpeak = onSpeak
            )
        }
    }
}

@Composable
fun ContentArea(
    pokemon: PokemonUI,
    onItemClick: (PokemonUI) -> Unit,
    onSpeak: ((String) -> Unit)?,
    selectedTab: String,
    onSelectedTabChange: (String) -> Unit
) {
    // Content area with rounded corners
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
            .background(PokedexColors.Background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight() // Ensure the column fills the height of the box
                .padding(vertical = 24.dp)
        ) {
            // Tabs for Info and Evolution
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TabItem("Info", selectedTab == "Info") { onSelectedTabChange("Info") }
                TabItem("Evolution", selectedTab == "Evolution") {
                    onSelectedTabChange("Evolution")
                }
            }
            Spacer(modifier = Modifier.height(PokedexDimens.SpacerHeight))

            // Content based on selected tab
            if (selectedTab == "Info") {
                StatArea(pokemon)
            } else if (selectedTab == "Evolution") {
                EvolutionArea(pokemon, onItemClick, onSpeak)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonViewInfo() {
    val pokemon = PokemonUI(
        id = 1,
        name = "Test",
        backgroundColorList = listOf("#FF4500"),
        typeList = listOf(
            TypeUI("Poison", "#FF4500", ""),
            TypeUI("Plante", "#8A2BE2", ""),
            TypeUI("Feu", "#32CD32", "")
        ),
        imageUrl = "",
        spriteUrl = "",
        previousEvolution = listOf(),
        nextEvolution = listOf(),
        statsUI = StatsUI(
            HP = 45, attack = 49, defense = 49,
            specialAttack = 65, specialDefense = 65, speed = 45
        )
    )
    PokemonDetailScreen(
        PokemonUI(
            id = 1,
            name = "Test",
            backgroundColorList = listOf("#FF4500"),
            typeList = listOf(
                TypeUI("Poison", "#FF4500", ""),
                TypeUI("Plante", "#8A2BE2", ""),
                TypeUI("Feu", "#32CD32", "")
            ),
            imageUrl = "",
            spriteUrl = "",
            previousEvolution = listOf(pokemon),
            nextEvolution = listOf(pokemon),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        defaultSelectedTab = "Info",
        onBack = {},
        onItemClick = {},
        onSpeak = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPokemonViewEvolution() {
    val pokemon = PokemonUI(
        id = 1,
        name = "Test",
        backgroundColorList = listOf("#FF4500"),
        typeList = listOf(
            TypeUI("Poison", "#FF4500", ""),
            TypeUI("Plante", "#8A2BE2", ""),
            TypeUI("Feu", "#32CD32", "")
        ),
        imageUrl = "",
        spriteUrl = "",
        previousEvolution = listOf(),
        nextEvolution = listOf(),
        statsUI = StatsUI(
            HP = 45, attack = 49, defense = 49,
            specialAttack = 65, specialDefense = 65, speed = 45
        )
    )
    PokemonDetailScreen(
        PokemonUI(
            id = 1,
            name = "Test",
            backgroundColorList = listOf("#FF4500"),
            typeList = listOf(
                TypeUI("Poison", "#FF4500", ""),
                TypeUI("Plante", "#8A2BE2", ""),
                TypeUI("Feu", "#32CD32", "")
            ),
            imageUrl = "",
            spriteUrl = "",
            previousEvolution = listOf(pokemon, pokemon),
            nextEvolution = listOf(pokemon, pokemon),
            statsUI = StatsUI(
                HP = 45, attack = 49, defense = 49,
                specialAttack = 65, specialDefense = 65, speed = 45
            )
        ),
        defaultSelectedTab = "Evolution",
        onBack = {},
        onItemClick = {},
        onSpeak = {}
    )
}