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
    @Published var pokemonList: [PokemonUI] = []
    @Published var isLoading: Bool = false

    let viewModel: PokemonViewModel
    private var cancellable: AnyCancellable?

    init() {
        viewModel = Shared().getPokemonViewModel()
        cancellable = createPublisher(for: viewModel.uiStateFlow)
            .replaceError(with: viewModel.uiState)
            .receive(on: DispatchQueue.main)
            .sink { [weak self] state in
                self?.pokemonList = state.pokemonUIList
                self?.isLoading = state.isLoading
            }
    }

    func loadPokemons() {
        print("loadPokemons called")
        viewModel.handleIntent(intent: PokemonIntent.LoadPokemonList.shared)
    }
}

struct ContentView: View {
    @StateObject private var viewModel = PokemonViewModelWrapper()
    var body: some View {
        NavigationView {
            List(viewModel.pokemonList, id: \.id) { pokemon in
                HStack {
                    AsyncImage(url: URL(string: pokemon.imageUrl)) { image in
                        image.resizable()
                    } placeholder: {
                        ProgressView()
                    }
                    .frame(width: 56, height: 56)
                    .clipShape(Circle())
                    VStack(alignment: .leading) {
                        Text("#\(pokemon.id) \(pokemon.name)")
                            .font(.headline)
                        Text(pokemon.colorList.joined(separator: ", "))
                            .font(.subheadline)
                            .foregroundColor(.gray)
                    }
                }
                .padding(.vertical, 4)
            }
            .navigationTitle("Pokedex")
            .onAppear { viewModel.loadPokemons() }
        }
    }
}

#Preview {
    ContentView()
}
