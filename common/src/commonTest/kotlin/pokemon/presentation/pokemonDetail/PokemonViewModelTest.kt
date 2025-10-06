package pokemon.presentation.pokemonDetail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pokemon.domain.model.Evolution
import pokemon.domain.model.Pokemon
import pokemon.domain.model.PreEvolution
import pokemon.domain.model.Stats
import pokemon.domain.usecase.FakeGetPokemonByIdUseCase
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


@OptIn(ExperimentalCoroutinesApi::class)
class PokemonViewModelTest {

    private lateinit var viewModel: PokemonViewModel
    private lateinit var getPokemonByIdUseCase: FakeGetPokemonByIdUseCase

    @BeforeTest
    fun setUp() {
        getPokemonByIdUseCase = FakeGetPokemonByIdUseCase()
        viewModel = PokemonViewModel(getPokemonByIdUseCase)
    }

    @Test
    fun `loadPokemonById should update state with pokemon details`() = runTest {
        // Arrange
        val mockPokemon = Pokemon(
            id = 1,
            pokedexId = 1,
            name = "Bulbasaur",
            typeList = emptyList(),
            imageUrl = "",
            sprite = "",
            generation = 1,
            slug = "",
            stats = Stats(
                HP = 45,
                attack = 49,
                defense = 49,
                specialAttack = 65,
                specialDefense = 65,
                speed = 45
            ),
            preEvolution = null,
            evolutions = emptyList(),
            resistances = emptyList()
        )
        getPokemonByIdUseCase.addPokemon(mockPokemon)

        // Act
        viewModel.handleIntent(PokemonIntent.LoadPokemonById(1))

        // Assert
        val state = viewModel.state.first()
        assertEquals("Bulbasaur", state.pokemonUI?.name)
    }

    @Test
    fun `loadPokemonById should handle pre-evolutions`() = runTest {
        // Arrange
        val mockPreEvolution = PreEvolution(name = "Ivysaur", pokedexId = 2)
        val mockPokemon = Pokemon(
            id = 1,
            pokedexId = 1,
            name = "Bulbasaur",
            typeList = emptyList(),
            imageUrl = "",
            sprite = "",
            generation = 1,
            slug = "",
            stats = Stats(
                HP = 45,
                attack = 49,
                defense = 49,
                specialAttack = 65,
                specialDefense = 65,
                speed = 45
            ),
            preEvolution = mockPreEvolution,
            evolutions = emptyList(),
            resistances = emptyList()
        )
        getPokemonByIdUseCase.addPokemon(mockPokemon)
        getPokemonByIdUseCase.addPokemon(mockPokemon.copy(id = 2, name = "Ivysaur"))

        // Act
        viewModel.handleIntent(PokemonIntent.LoadPokemonById(1))

        // Assert
        val state = viewModel.state.first()
        assertEquals("Ivysaur", state.pokemonUI?.previousEvolution?.first()?.name)
    }

    @Test
    fun `loadPokemonById should handle evolutions`() = runTest {
        // Arrange
        val mockEvolution = Evolution(name = "Venusaur", pokedexId = 3)
        val mockPokemon = Pokemon(
            id = 1,
            pokedexId = 1,
            name = "Bulbasaur",
            typeList = emptyList(),
            imageUrl = "",
            sprite = "",
            generation = 1,
            slug = "",
            stats = Stats(
                HP = 45,
                attack = 49,
                defense = 49,
                specialAttack = 65,
                specialDefense = 65,
                speed = 45
            ),
            preEvolution = null,
            evolutions = listOf(mockEvolution),
            resistances = emptyList()
        )
        getPokemonByIdUseCase.addPokemon(mockPokemon)
        getPokemonByIdUseCase.addPokemon(mockPokemon.copy(id = 3, name = "Venusaur"))

        // Act
        viewModel.handleIntent(PokemonIntent.LoadPokemonById(1))

        // Assert
        val state = viewModel.state.first()
        assertEquals("Venusaur", state.pokemonUI?.nextEvolution?.first()?.name)
    }
}