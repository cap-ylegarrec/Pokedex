# 🤝 Comprendre Kotlin Multiplatform pour les développeurs iOS

Ce guide est destiné aux développeurs iOS Swift/SwiftUI qui souhaitent comprendre comment Kotlin Multiplatform (KMP) expose la logique métier partagée à l'environnement iOS.

---

## 🔷 `data class` Kotlin = `struct` Swift

En Kotlin :
```kotlin
data class Pokemon(val name: String, val type: String)
```

En Swift :
```swift
struct Pokemon: Equatable {
    let name: String
    let type: String
}
```

### ✅ Ce que vous devez savoir :
- Les `data class` Kotlin sont automatiquement converties en types Swift utilisables
- Elles sont immuables et comparables comme les `struct`
- Très facile à afficher dans SwiftUI

---

## 🔶 `sealed class` Kotlin = enum Swift avec valeurs associées (mais…)

En Kotlin :
```kotlin
sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Pokemon>) : UiState()
    data class Error(val message: String) : UiState()
}
```

En Swift (idéalement) :
```swift
enum UiState {
    case loading
    case success([Pokemon])
    case error(String)
}
```

### ⚠️ En réalité dans KMP :
Kotlin génère une **classe mère abstraite** + **sous-classes**, donc en Swift :
```swift
if let success = state as? UiStateSuccess {
    // utiliser success.data
} else if state is UiStateLoading {
    // afficher un loader
}
```

> 💡 **Pas aussi fluide qu’un `enum` Swift**, mais reste parfaitement utilisable.

---

## 🔁 Recommandation pour simplifier le code Swift

Si vous voulez une intégration fluide dans SwiftUI, demandez au développeur KMP de vous exposer un `data class` comme `UiStateDTO`, par exemple :

```kotlin
data class UiStateDTO(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val pokemons: List<Pokemon> = emptyList()
)
```

Côté Swift :
```swift
@Published var state: UiStateDTO
```

Et dans SwiftUI :
```swift
if state.isLoading {
    ProgressView()
} else {
    List(state.pokemons, id: \.name) { pokemon in
        Text(pokemon.name)
    }
}
```

✅ Lecture directe, sans casting ni `as?`.

---

## 📡 Observabilité avec KMP-NativeCoroutines

Le code Kotlin peut exposer des `StateFlow`, que Swift peut consommer via Combine :

```kotlin
@NativeCoroutinesState
val state: StateFlow<UiStateDTO>
```

Et côté Swift :
```swift
createPublisher(for: viewModel.state)
    .receive(on: DispatchQueue.main)
    .sink { [weak self] newState in
        self?.state = newState
    }
```

---

## 🔐 Résumé des bonnes pratiques iOS avec KMP

| Kotlin                      | Swift                       | Recommandé ? |
|----------------------------|-----------------------------|--------------|
| `data class`               | `struct`                    | ✅ Oui       |
| `sealed class`             | `class` + cast `as?`        | ⚠️ Moyennement |
| `StateFlow<T>`             | `@Published` avec Combine   | ✅ Oui       |
| `suspend fun`              | `async/await` via wrapper   | ✅ Oui       |

---

## 🙋 Questions fréquentes

**Q : Puis-je utiliser SwiftUI avec des `StateFlow` KMP ?**  
Oui, via `@Published` + `KMP-NativeCoroutinesCombine`.

**Q : Comment faire si le `sealed class` est trop complexe ?**  
Demander une version `data class` intermédiaire plus facile à mapper.

**Q : Peut-on appeler une `suspend fun` Kotlin depuis Swift ?**  
Oui, avec `@NativeCoroutines` + `async/await` via `KMP-NativeCoroutines`.

---

Ce guide a pour but de **démystifier KMP côté iOS**.  
En cas de doute, collaborez avec le dev KMP pour simplifier l’interopérabilité.  
💬 N'hésitez pas à ouvrir une issue ou pinguer un dev Android/KMP en cas de blocage !
