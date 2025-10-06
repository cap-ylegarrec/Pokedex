package pokemon.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import pokemon.data.repositoryimpl.FakePokemonRepositoryImpl
import pokemon.domain.model.Pokemon
import pokemon.domain.model.Stats
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetPokemonListUseCaseTest {

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

    private lateinit var useCase: GetPokemonListUseCase
    private lateinit var repository: FakePokemonRepositoryImpl

    @BeforeTest
    fun setUp() {
        repository = FakePokemonRepositoryImpl()
        useCase = GetPokemonListUseCase(repository)
    }

    @Test
    fun `execute should return pokemon list for a generation`() = runTest {
        // Arrange
        repository.addPokemon(mockPokemonList[0])
        repository.addPokemon(mockPokemonList[1])

        // Act
        val result = useCase.execute(1)

        // Assert
        assertEquals(2, result.size)
        assertEquals("Bulbasaur", result[0].name)
    }
}