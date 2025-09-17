//
//  PokedexCuscatlanApp.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

@main
struct PokedexCuscatlanApp: App {
    @State private var showingProfile = false

    var body: some Scene {
        WindowGroup {
            NavigationStack {
                PokedexView(showingProfile: $showingProfile)
                    .navigationDestination(for: Int.self) { pokemonId in
                        PokemonDetailView(pokemonId: pokemonId)
                    }
            }
        }
    }
}