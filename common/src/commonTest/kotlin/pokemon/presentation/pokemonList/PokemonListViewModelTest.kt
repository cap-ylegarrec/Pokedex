package pokemon.presentation.pokemonList

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pokemon.domain.model.Pokemon
import pokemon.domain.model.Stats
import pokemon.domain.usecase.FakeGetPokemonListUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonListViewModelTest {

    val mockPokemonList = listOf(
        Pokemon(
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
            evolutions = emptyList(),
            preEvolution = null,
            resistances = emptyList()
        ),
        Pokemon(
            id = 2,
            pokedexId = 2,
            name = "Ivysaur",
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
            evolutions = emptyList(),
            preEvolution = null,
            resistances = emptyList()
        )
    )


    private lateinit var viewModel: PokemonListViewModel
    private lateinit var getPokemonListUseCase: FakeGetPokemonListUseCase

    @BeforeTest
    fun setUp() {
        getPokemonListUseCase = FakeGetPokemonListUseCase()
        viewModel = PokemonListViewModel(getPokemonListUseCase)
    }

    @Test
    fun `loadPokemonList should update state with pokemon list`() = runTest {
        // Arrange
        mockPokemonList.forEach { getPokemonListUseCase.addPokemon(it) }

        // Act
        viewModel.handleIntent(PokemonListIntent.LoadPokemonList)

        // Assert
        val state = viewModel.state.first()
        assertEquals(2, state.pokemonUIList.size)
        assertEquals("Bulbasaur", state.pokemonUIList[0].name)
    }

    @Test
    fun `searchedPokemonFromList should update searchedPokemonPosition`() = runTest {
        // Arrange
        mockPokemonList.forEach { getPokemonListUseCase.addPokemon(it) }
        viewModel.handleIntent(PokemonListIntent.LoadPokemonList)

        // Act
        viewModel.handleIntent(PokemonListIntent.SearchPokemon("Ivy"))

        // Assert
        val position = viewModel.searchedPokemonPosition.first()
        assertEquals(1, position)
    }
}