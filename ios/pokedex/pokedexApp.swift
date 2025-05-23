//
//  pokedexApp.swift
//  pokedex
//
//  Created by Yann LE GARREC on 21/05/2025.
//

import SwiftUI
import shared

@main
struct pokedexApp: App {
    init() {
        Shared().initialize();
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
