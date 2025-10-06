package pokemon.data.repositoryimpl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import pokemon.data.datasource.FakePokemonLocalDataSource
import pokemon.data.datasource.FakePokemonRemoteDataSource
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import pokemon.domain.model.Pokemon
import pokemon.domain.model.Stats

@OptIn(ExperimentalCoroutinesApi::class)
class PokemonRepositoryImplTest {
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

    private lateinit var repository: PokemonRepositoryImpl
    private lateinit var localDataSource: FakePokemonLocalDataSource
    private lateinit var remoteDataSource: FakePokemonRemoteDataSource

    @BeforeTest
    fun setUp() {
        localDataSource = FakePokemonLocalDataSource()
        remoteDataSource = FakePokemonRemoteDataSource()
        repository = PokemonRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getPokemonListByGeneration should return cached data if available`() = runTest {
        // Arrange
        mockPokemonList.forEach { localDataSource.addPokemon(it) }

        // Act
        val result = repository.getPokemonListByGeneration(1)

        // Assert
        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result[0].name)
    }

    @Test
    fun `getPokemonListByGeneration should fetch from remote if cache is empty`() = runTest {
        // Arrange
        mockPokemonList.forEach { remoteDataSource.addPokemon(it) }

        // Act
        val result = repository.getPokemonListByGeneration(1)

        // Assert
        assertEquals(1, result.size)
        assertEquals("Bulbasaur", result[0].name)
    }

    @Test
    fun `getPokemonById should return data from local source`() = runTest {
        // Arrange
        localDataSource.addPokemon(mockPokemonList[0])

        // Act
        val result = repository.getPokemonById(1)

        // Assert
        assertEquals("Bulbasaur", result?.name)
    }
}