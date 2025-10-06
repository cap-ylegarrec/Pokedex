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
class GetPokemonByIdUseCaseTest {

    private lateinit var useCase: GetPokemonByIdUseCase
    private lateinit var repository: FakePokemonRepositoryImpl

    @BeforeTest
    fun setUp() {
        repository = FakePokemonRepositoryImpl()
        useCase = GetPokemonByIdUseCase(repository)
    }

    @Test
    fun `execute should return pokemon by id`() = runTest {
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
        repository.addPokemon(mockPokemon)

        // Act
        val result = useCase.execute(1)

        // Assert
        assertEquals("Bulbasaur", result?.name)
    }
}