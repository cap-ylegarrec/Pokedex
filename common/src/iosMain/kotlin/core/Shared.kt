import di.commonModule
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.context.startKoin
import org.koin.core.parameter.parametersOf
import pokemon.presentation.PokemonViewModel

class Shared : KoinComponent {

    fun initialize(
    ) {
        startKoin {
            modules(commonModule)
        }
    }

    fun getPokemonViewModel(): PokemonViewModel {
        return get()
    }

}