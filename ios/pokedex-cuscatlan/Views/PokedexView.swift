//
//  PokedexView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

struct PokedexView: View {
    @StateObject private var viewModel = PokemonListViewModel()
    @Binding var showingProfile: Bool

    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        VStack(spacing: 0) {
            // Header with Pokédex logo and greeting
            HStack {
                HStack {
                    // Pokéball icon
                    ZStack {
                        Circle()
                            .fill(Color(hex: "3B82F6"))
                            .frame(width: 40, height: 40)

                        Text("⚪")
                            .font(.title2)
                            .foregroundColor(.white)
                    }

                    Text("Pokédex")
                        .font(.title)
                        .fontWeight(.bold)
                        .foregroundColor(Color(hex: "3B82F6"))
                }

                Spacer()

                // Profile button
                Button(action: {
                    showingProfile = true
                }) {
                    ZStack {
                        Circle()
                            .fill(Color(hex: "3B82F6").opacity(0.1))
                            .frame(width: 40, height: 40)

                        Image(systemName: "person.circle")
                            .font(.title2)
                            .foregroundColor(Color(hex: "3B82F6"))
                    }
                }
            }
            .padding(.horizontal, 16)
            .padding(.vertical, 16)

            // Greeting
            HStack {
                Text("¡Hola, bienvenido!")
                    .font(.title2)
                    .fontWeight(.bold)
                    .foregroundColor(.black)
                Spacer()
            }
            .padding(.horizontal, 16)
            .padding(.bottom, 8)

            // Search Bar
            HStack {
                Image(systemName: "magnifyingglass")
                    .foregroundColor(Color(hex: "FFB000"))
                    .padding(.leading, 12)

                TextField("Buscar", text: $viewModel.searchQuery)
                    .textFieldStyle(PlainTextFieldStyle())
                    .padding(.vertical, 12)
            }
            .background(Color.white)
            .overlay(
                RoundedRectangle(cornerRadius: 25)
                    .stroke(viewModel.searchQuery.isEmpty ? Color.gray.opacity(0.3) : Color(hex: "FFB000"), lineWidth: 1)
            )
            .cornerRadius(25)
            .padding(.horizontal, 16)
            .padding(.bottom, 16)

            // Error Display
            if let error = viewModel.error {
                VStack {
                    HStack {
                        Text(error)
                            .foregroundColor(.red)
                            .padding()
                        Spacer()
                        Button("Reintentar") {
                            viewModel.clearError()
                            viewModel.retry()
                        }
                        .foregroundColor(Color(hex: "FFB000"))
                        .padding()
                    }
                    .background(Color.red.opacity(0.1))
                    .cornerRadius(8)
                    .padding(.horizontal, 16)
                }
            }

            // Loading State
            if viewModel.isLoading {
                Spacer()
                VStack {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "3B82F6")))
                        .scaleEffect(1.5)
                    Text("Cargando Pokémon...")
                        .foregroundColor(.black)
                        .padding(.top, 16)
                }
                Spacer()
            } else {
                // Pokemon Grid
                ScrollView {
                    LazyVGrid(columns: columns, spacing: 12) {
                        ForEach(viewModel.filteredPokemonList) { pokemon in
                            NavigationLink(value: pokemon.id) {
                                PokemonGridItem(
                                    pokemon: pokemon,
                                    pokemonData: viewModel.pokemonDetails[pokemon.id],
                                    onLoadDetails: {
                                        viewModel.loadPokemonDetails(id: pokemon.id)
                                    }
                                )
                            }
                            .buttonStyle(PlainButtonStyle())
                            .simultaneousGesture(TapGesture().onEnded {
                                print("🎯 Pokemon card tapped: \(pokemon.name) (ID: \(pokemon.id))")
                            })
                        }
                    }
                    .padding(.horizontal, 16)
                }

            }
        }
        .background(Color(hex: "F8F8F8"))
        .onAppear {
            print("🎮 PokedexView appeared")
            print("📊 Pokemon list count: \(viewModel.pokemonList.count)")
            print("🔍 Filtered list count: \(viewModel.filteredPokemonList.count)")
            if viewModel.pokemonList.isEmpty {
                print("🔄 Loading Pokemon list from PokedexView...")
                viewModel.loadPokemonList()
            }
        }
        .sheet(isPresented: $showingProfile) {
            OptionalTrainerFormView(
                isPresented: $showingProfile,
                onTrainerSaved: .constant { trainer in
                    // Handle trainer saved if needed
                }
            )
        }
    }
}

struct PokemonGridItem: View {
    let pokemon: PokemonListItem
    let pokemonData: PokemonDisplayData?
    let onLoadDetails: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            // Pokemon number
            HStack {
                Spacer()
                Text("#\(String(format: "%03d", pokemon.id))")
                    .font(.caption)
                    .foregroundColor(.gray)
            }
            .padding(.horizontal, 12)
            .padding(.top, 12)

            Spacer(minLength: 8)

            // Pokemon image
            VStack {
                if let pokemonData = pokemonData {
                    AsyncImage(url: URL(string: pokemonData.imageUrl)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                    } placeholder: {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "3B82F6")))
                    }
                    .frame(width: 80, height: 80)
                } else {
                    ZStack {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color(hex: "FFB000").opacity(0.1))
                            .frame(width: 80, height: 80)

                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "3B82F6")))
                            .scaleEffect(0.8)
                    }
                    .onAppear {
                        print("🔍 Loading details for Pokemon: \(pokemon.name) (ID: \(pokemon.id))")
                        onLoadDetails()
                    }
                }
            }

            Spacer(minLength: 8)

            // Pokemon name
            Text(pokemon.name.capitalized)
                .font(.body)
                .fontWeight(.medium)
                .foregroundColor(.black)
                .multilineTextAlignment(.center)
                .lineLimit(1)
                .padding(.horizontal, 12)
                .padding(.bottom, 12)
        }
        .frame(maxWidth: .infinity)
        .aspectRatio(0.85, contentMode: .fit)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
        .overlay(
            RoundedRectangle(cornerRadius: 16)
                .stroke(Color(hex: "3B82F6").opacity(0.1), lineWidth: 1)
        )
    }
}

#Preview {
    NavigationView {
        PokedexView(showingProfile: .constant(false))
    }
}