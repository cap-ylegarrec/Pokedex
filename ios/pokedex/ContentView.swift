//
//  ContentView.swift
//  pokedex
//
//  Created by Yann LE GARREC on 21/05/2025.
//

import SwiftUI
import Combine
import shared
import KMPNativeCoroutinesCombine

class PokemonViewModelWrapper: ObservableObject {
    @Published var uiState: PokemonState = PokemonState(pokemonUIList: [], isLoading: true, isSortedAlphabetically: false)

    let viewModel: PokemonViewModel
    private var cancellable: AnyCancellable?

    init() {
        viewModel = Shared().getPokemonViewModel()
        cancellable = createPublisher(for: viewModel.uiStateFlow)
            .replaceError(with: viewModel.uiState)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] state in
                self?.uiState = state
            }
    }

    func handleIntent(intent: PokemonIntent) {
        viewModel.handleIntent(intent: intent)
    }
}

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
                    .disabled(viewModel.uiState.isLoading)
                }
            }
        }
        .onAppear {
            viewModel.handleIntent(intent: PokemonIntent.LoadPokemonList.shared)
        }
    }
}

#Preview {
    ContentView()
}
