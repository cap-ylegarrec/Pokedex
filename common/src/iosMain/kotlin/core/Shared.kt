import di.commonModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import pokemon.presentation.pokemonDetail.PokemonViewModel
import pokemon.presentation.pokemonList.PokemonListViewModel

class Shared : KoinComponent {

    fun initialize(
    ) {
        startKoin {
            modules(iOSCommonModule)
        }
    }

    fun getPokemonViewModel(): PokemonViewModel {
        return get()
    }

    fun getPokemonListViewModel(): PokemonListViewModel {
        return get()
    }

}