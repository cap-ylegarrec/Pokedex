# 📱 Implémentation du Bouton Refresh avec Tri Alphabétique

## 🎯 Objectif
Ajouter un bouton refresh qui permet de trier les Pokémon par ordre alphabétique dans l'application Pokédex iOS/Android basée sur Kotlin Multiplatform (KMP).

## 📋 Étapes détaillées de l'implémentation

---

## **🎯 Étape 1 : Création du nouveau Use Case**

**Fichier créé :** `common/src/commonMain/kotlin/pokemon/domain/usecase/GetPokemonListSortedUseCase.kt`

```kotlin
package pokemon.domain.usecase

import com.rickclephas.kmp.nativecoroutines.NativeCoroutines
import pokemon.domain.model.Pokemon
import pokemon.domain.repository.PokemonRepository

class GetPokemonListSortedUseCase(
    private val repository: PokemonRepository
) {
    @NativeCoroutines
    suspend fun execute(): List<Pokemon> {
        return repository.getPokemonList().sortedBy { it.name }
    }
}
```

**🔍 Explication :**
- **Pourquoi ?** : Respecter l'architecture Clean en séparant la logique métier
- **Rôle** : Récupérer les Pokémon et les trier par nom alphabétiquement
- **`@NativeCoroutines`** : Permet l'interopérabilité avec Swift/iOS
- **`sortedBy { it.name }`** : Trie la liste par le nom des Pokémon

---

## **🎯 Étape 2 : Ajout d'une nouvelle intention**

**Fichier modifié :** `common/src/commonMain/kotlin/pokemon/presentation/PokemonIntent.kt`

```kotlin
package pokemon.presentation

sealed class PokemonIntent {
    data object LoadPokemonList : PokemonIntent()
    data object LoadPokemonListSorted : PokemonIntent()  // ← NOUVEAU
}
```

**🔍 Explication :**
- **Pourquoi ?** : Pattern MVI (Model-View-Intent) - chaque action utilisateur = une intention
- **Rôle** : Représenter l'action "charger les Pokémon triés"
- **`data object`** : Singleton Kotlin, pas besoin de paramètres

---

## **🎯 Étape 3 : Enrichissement de l'état**

**Fichier modifié :** `common/src/commonMain/kotlin/pokemon/presentation/PokemonState.kt`

```kotlin
package pokemon.presentation

data class PokemonState(
    val pokemonUIList: List<PokemonUI> = emptyList(), 
    val isLoading: Boolean = false,
    val isSortedAlphabetically: Boolean = false  // ← NOUVEAU
)
```

**🔍 Explication :**
- **Pourquoi ?** : L'UI doit savoir dans quel état elle se trouve pour afficher le bon bouton
- **Rôle** : Indiquer si la liste actuelle est triée alphabétiquement ou non
- **Immutable** : `data class` garantit l'immutabilité de l'état

---

## **🎯 Étape 4 : Mise à jour du ViewModel**

**Fichier modifié :** `common/src/commonMain/kotlin/pokemon/presentation/PokemonViewModel.kt`

### **4a. Ajout du nouveau Use Case dans le constructeur**
```kotlin
class PokemonViewModel(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val getPokemonListSortedUseCase: GetPokemonListSortedUseCase  // ← NOUVEAU
) : ViewModel()
```

### **4b. Gestion de la nouvelle intention**
```kotlin
fun handleIntent(intent: PokemonIntent) {
    when (intent) {
        is PokemonIntent.LoadPokemonList -> viewModelScope.launch {
            loadPokemonList()
        }
        is PokemonIntent.LoadPokemonListSorted -> viewModelScope.launch {  // ← NOUVEAU
            loadPokemonListSorted()
        }
    }
}
```

### **4c. Nouvelle méthode de chargement trié**
```kotlin
private suspend fun loadPokemonListSorted() {
    _uiState.value = _uiState.value.copy(isLoading = true)
    val pokemonList = getPokemonListSortedUseCase.execute()
    _uiState.value = PokemonState(
        pokemonUIList = pokemonList.map { pokemon -> PokemonUI.from(pokemon) },
        isLoading = false,
        isSortedAlphabetically = true  // ← Important !
    )
}
```

### **4d. Mise à jour de la méthode existante**
```kotlin
private suspend fun loadPokemonList() {
    _uiState.value = PokemonState(isLoading = true)
    val pokemonList = getPokemonListUseCase.execute()
    _uiState.value = PokemonState(
        pokemonUIList = pokemonList.map { pokemon -> PokemonUI.from(pokemon) },
        isLoading = false,
        isSortedAlphabetically = false  // ← Ajouté
    )
}
```

**🔍 Explication :**
- **Pourquoi ?** : Le ViewModel orchestre la logique de présentation
- **Pattern** : Chaque intention déclenche une action spécifique
- **État de chargement** : Copie l'état actuel et met `isLoading = true`
- **Mapping** : Conversion des entités domain vers les entités UI

---

## **🎯 Étape 5 : Mise à jour de l'injection de dépendances**

**Fichier modifié :** `common/src/commonMain/kotlin/di/CommonModule.kt`

```kotlin
package di

import org.koin.core.module.Module
import org.koin.dsl.module
import pokemon.data.datasource.PokemonRemoteDataSource
import pokemon.data.repositoryimpl.PokemonRepositoryImpl
import pokemon.domain.repository.PokemonRepository
import pokemon.domain.usecase.GetPokemonListUseCase
import pokemon.domain.usecase.GetPokemonListSortedUseCase  // ← NOUVEAU
import pokemon.presentation.PokemonViewModel

val commonModule: Module = module {
    single<PokemonRepository> { PokemonRepositoryImpl(get()) }
    single { PokemonRemoteDataSource() }
    single { GetPokemonListUseCase(get()) }
    single { GetPokemonListSortedUseCase(get()) }  // ← NOUVEAU
    single { PokemonViewModel(get(), get()) }      // ← Modifié (2 paramètres)
}
```

**🔍 Explication :**
- **Pourquoi ?** : Koin doit connaître toutes les dépendances à injecter
- **`single`** : Instance unique (singleton) partagée dans l'app
- **`get()`** : Koin résout automatiquement les dépendances
- **Ordre important** : Les dépendances doivent être déclarées avant leur utilisation

---

## **🎯 Étape 6 : Mise à jour de l'interface SwiftUI**

**Fichier modifié :** `ios/pokedex/ContentView.swift`

### **6a. Mise à jour du Wrapper**
```swift
class PokemonViewModelWrapper: ObservableObject {
    @Published var uiState: PokemonState = PokemonState(
        pokemonUIList: [], 
        isLoading: true, 
        isSortedAlphabetically: false  // ← NOUVEAU
    )

    let viewModel: PokemonViewModel
    private var cancellable: AnyCancellable?

    init() {
        viewModel = Shared().getPokemonViewModel()
        cancellable = createPublisher(for: viewModel.uiStateFlow)
            .replaceError(with: viewModel.uiState)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] state in
                self?.uiState = state  // ← Simplifié
            }
    }

    func handleIntent(intent: PokemonIntent) {  // ← Méthode générique
        viewModel.handleIntent(intent: intent)
    }
}
```

### **6b. Ajout du bouton intelligent dans la toolbar**
```swift
struct ContentView: View {
    @StateObject private var viewModel = PokemonViewModelWrapper()
    
    var body: some View {
        NavigationView {
            VStack {
                if viewModel.uiState.isLoading {
                    ProgressView("Chargement des Pokémon...")
                        .padding()
                } else {
                    List(viewModel.uiState.pokemonUIList, id: \.name) { pokemon in
                        HStack {
                            AsyncImage(url: URL(string: pokemon.imageUrl)) { image in
                                image
                                    .resizable()
                                    .aspectRatio(contentMode: .fit)
                            } placeholder: {
                                ProgressView()
                            }
                            .frame(width: 60, height: 60)
                            
                            VStack(alignment: .leading) {
                                Text(pokemon.name.capitalized)
                                    .font(.headline)
                                Text("ID: \(pokemon.id)")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                            
                            Spacer()
                        }
                        .padding(.vertical, 4)
                    }
                }
            }
            .navigationTitle("Pokédex")
            .toolbar {
                ToolbarItem(placement: .navigationBarTrailing) {
                    Button(action: {
                        if viewModel.uiState.isSortedAlphabetically {
                            viewModel.handleIntent(intent: PokemonIntent.LoadPokemonList.shared)
                        } else {
                            viewModel.handleIntent(intent: PokemonIntent.LoadPokemonListSorted.shared)
                        }
                    }) {
                        HStack {
                            Image(systemName: viewModel.uiState.isSortedAlphabetically ? "arrow.up.arrow.down" : "textformat.abc")
                            Text(viewModel.uiState.isSortedAlphabetically ? "Original" : "A-Z")
                        }
                    }
                    .disabled(viewModel.uiState.isLoading)  // ← Désactivé pendant le chargement
                }
            }
        }
        .onAppear {
            viewModel.handleIntent(intent: PokemonIntent.LoadPokemonList.shared)
        }
    }
}
```

**🔍 Explication :**
- **Bouton conditionnel** : Change d'apparence selon l'état
- **Icônes SF Symbols** : `textformat.abc` (A-Z) et `arrow.up.arrow.down` (tri)
- **`.shared`** : Accès aux singletons Kotlin depuis Swift
- **`.disabled()`** : UX - empêche les clics multiples pendant le chargement

---

## **🎯 Étape 7 : Compilation et déploiement**

### **7a. Nettoyage du projet**
```bash
./gradlew clean
```

### **7b. Compilation du framework Kotlin**
```bash
./gradlew :common:assembleReleaseXCFramework
```

### **7c. Compilation du projet iOS**
```bash
cd ios && xcodebuild -workspace pokedex.xcworkspace -scheme pokedex -configuration Debug -destination 'platform=iOS Simulator,name=iPhone 16,OS=18.2' build
```

### **7d. Installation et lancement**
```bash
xcrun simctl boot "iPhone 16"
xcrun simctl install "iPhone 16" [chemin_vers_app]
xcrun simctl launch "iPhone 16" com.legarrec.pokedex
```

**🔍 Explication :**
- **Clean** : Évite les conflits de cache
- **XCFramework** : Format Apple pour les frameworks multiplateformes
- **Workspace** : Nécessaire car on utilise CocoaPods
- **Simulateur** : Test sur iPhone 16 avec iOS 18.2

---

## **🏗️ Architecture finale**

```
┌─────────────────────────────────────────────────────────────┐
│                        UI Layer (iOS)                       │
│  ┌─────────────────┐    ┌─────────────────────────────────┐ │
│  │   ContentView   │────│  PokemonViewModelWrapper        │ │
│  │   (SwiftUI)     │    │  (@Published uiState)          │ │
│  └─────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────┐
│                   Presentation Layer (KMP)                  │
│  ┌─────────────────┐    ┌─────────────────────────────────┐ │
│  │ PokemonViewModel│────│         PokemonState            │ │
│  │ (StateFlow)     │    │  • pokemonUIList                │ │
│  │                 │    │  • isLoading                    │ │
│  │                 │    │  • isSortedAlphabetically       │ │
│  └─────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────┐
│                    Domain Layer (KMP)                       │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                   Use Cases                             │ │
│  │  • GetPokemonListUseCase                                │ │
│  │  • GetPokemonListSortedUseCase                          │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────┐
│                     Data Layer (KMP)                        │
│  ┌─────────────────┐    ┌─────────────────────────────────┐ │
│  │PokemonRepository│────│    PokemonRemoteDataSource      │ │
│  │   (Interface)   │    │         (Ktor HTTP)             │ │
│  └─────────────────┘    └─────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
```

## **🔧 Nouvelles fonctionnalités ajoutées**

1. **Nouveau Use Case** (`GetPokemonListSortedUseCase`) :
   - Récupère les Pokémon triés par ordre alphabétique
   - Utilise `sortedBy { it.name }` pour le tri

2. **Nouvelle intention** (`LoadPokemonListSorted`) :
   - Permet de déclencher le chargement des Pokémon triés

3. **État enrichi** (`PokemonState`) :
   - Ajout du champ `isSortedAlphabetically` pour indiquer l'état du tri

4. **ViewModel mis à jour** :
   - Gestion des deux intentions (normale et triée)
   - Logique de tri intégrée

5. **Interface SwiftUI améliorée** :
   - **Bouton intelligent** dans la toolbar qui change d'icône et de texte :
     - 🔤 **"A-Z"** quand la liste est normale → trie alphabétiquement
     - ↕️ **"Original"** quand la liste est triée → revient à l'ordre original
   - Le bouton est désactivé pendant le chargement
   - Interface utilisateur intuitive et moderne

## **🎯 Fonctionnement**

- **Au démarrage** : La liste se charge dans l'ordre original de l'API
- **Clic sur "A-Z"** : Trie les Pokémon par ordre alphabétique
- **Clic sur "Original"** : Revient à l'ordre original de l'API
- **Indicateur visuel** : L'icône et le texte changent selon l'état actuel

## **✅ Résultat final**

L'utilisateur peut maintenant :
1. **Voir la liste** des Pokémon dans l'ordre original
2. **Cliquer sur "A-Z"** pour trier alphabétiquement
3. **Cliquer sur "Original"** pour revenir à l'ordre initial
4. **Voir l'état** grâce aux icônes et textes qui changent
5. **Expérience fluide** avec indicateur de chargement

## **🏛️ Principes respectés**

✅ **Clean Architecture** : Use Case → Repository → DataSource  
✅ **MVVM** : ViewModel gère la logique, UI observe les changements  
✅ **Kotlin Multiplatform** : Logique partagée entre iOS et Android  
✅ **Reactive Programming** : StateFlow pour la réactivité  
✅ **SOLID** : Séparation des responsabilités  
✅ **MVI Pattern** : Model-View-Intent pour la gestion d'état  

Chaque étape respecte les principes de **Clean Architecture**, **SOLID**, et **Kotlin Multiplatform** ! 🚀 