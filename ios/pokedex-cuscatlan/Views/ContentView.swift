//
//  ContentView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

struct ContentView: View {
    @State private var showingProfile = false

    var body: some View {
        NavigationStack {
            VStack {
                Text("¡Hola! Pokédx Cuscatlán")
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(Color(hex: "3B82F6"))
                    .padding()

                Text("App cargando correctamente")
                    .font(.body)
                    .foregroundColor(.gray)
                    .padding()

                NavigationLink(destination: PokedexView(showingProfile: $showingProfile)) {
                    Text("Ir al Pokédx")
                        .padding()
                        .background(Color(hex: "FFB000"))
                        .foregroundColor(.white)
                        .cornerRadius(8)
                }

                Spacer()
            }
            .background(Color(hex: "F8F8F8"))
            .navigationDestination(for: Int.self) { pokemonId in
                PokemonDetailView(pokemonId: pokemonId)
            }
        }
    }
}

#Preview {
    ContentView()
}