//
//  PokemonSelectionView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

struct PokemonSelectionView: View {
    @StateObject private var viewModel = PokemonListViewModel()
    @Binding var isPresented: Bool
    @Binding var onPokemonSelected: ([PokemonDisplayData]) -> Void

    let columns = [
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        NavigationView {
            VStack(spacing: 16) {
                // Header with back button
                HStack {
                    Button(action: {
                        isPresented = false
                    }) {
                        ZStack {
                            Circle()
                                .fill(Color(hex: "FFB000").opacity(0.1))
                                .frame(width: 40, height: 40)

                            Image(systemName: "arrow.left")
                                .font(.title2)
                                .foregroundColor(Color(hex: "FFB000"))
                        }
                    }

                    VStack(alignment: .leading, spacing: 4) {
                        Text("Selecciona tu equipo Pokémon")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.black)
                    }

                    Spacer()
                }
                .padding(.horizontal, 16)

                Text("Elige 3 Pokémon de la primera generación")
                    .font(.body)
                    .foregroundColor(.gray)
                    .frame(maxWidth: .infinity, alignment: .leading)
                    .padding(.horizontal, 16)

                // Selection Counter
                HStack {
                    Text("Seleccionados: \(viewModel.selectedPokemon.count)/3")
                        .font(.body)
                        .fontWeight(.medium)
                        .foregroundColor(Color(hex: "FFB000"))

                    Spacer()

                    if !viewModel.selectedPokemon.isEmpty {
                        Button("Limpiar todo") {
                            viewModel.clearSelection()
                        }
                        .foregroundColor(Color(hex: "FFB000"))
                    }
                }
                .padding()
                .background(Color(hex: "FFF8E1"))
                .cornerRadius(16)
                .padding(.horizontal, 16)

                // Search Bar
                HStack {
                    Image(systemName: "magnifyingglass")
                        .foregroundColor(Color(hex: "FFB000"))
                        .padding(.leading, 12)

                    TextField("Buscar Pokémon por nombre o ID", text: $viewModel.searchQuery)
                        .textFieldStyle(PlainTextFieldStyle())
                        .padding(.vertical, 12)

                    if !viewModel.searchQuery.isEmpty {
                        Button(action: {
                            viewModel.updateSearchQuery("")
                        }) {
                            Image(systemName: "xmark.circle.fill")
                                .foregroundColor(.gray)
                        }
                        .padding(.trailing, 12)
                    }
                }
                .background(Color.white)
                .overlay(
                    RoundedRectangle(cornerRadius: 25)
                        .stroke(viewModel.searchQuery.isEmpty ? Color.gray.opacity(0.3) : Color(hex: "FFB000"), lineWidth: 1)
                )
                .cornerRadius(25)
                .padding(.horizontal, 16)

                // Continue Button
                if viewModel.selectedPokemon.count == 3 {
                    Button(action: {
                        onPokemonSelected(viewModel.selectedPokemonData)
                        isPresented = false
                    }) {
                        Text("Continuar con el equipo seleccionado")
                            .font(.body)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(Color(hex: "FFB000"))
                            .cornerRadius(25)
                    }
                    .padding(.horizontal, 16)
                }

                // Error Display
                if let error = viewModel.error {
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

                // Loading State
                if viewModel.isLoading {
                    Spacer()
                    VStack {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "FFB000")))
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
                                PokemonSelectionCard(
                                    pokemon: pokemon,
                                    pokemonData: viewModel.pokemonDetails[pokemon.id],
                                    isSelected: viewModel.selectedPokemon.contains(pokemon.id),
                                    onTap: {
                                        print("🐱 Pokemon tapped: \(pokemon.name) (ID: \(pokemon.id))")
                                        print("🎯 Selected Pokemon count: \(viewModel.selectedPokemon.count)")
                                        print("🔍 Can select: \(viewModel.selectedPokemon.count < 3 || viewModel.selectedPokemon.contains(pokemon.id))")
                                        if viewModel.selectedPokemon.count < 3 || viewModel.selectedPokemon.contains(pokemon.id) {
                                            viewModel.togglePokemonSelection(pokemon.id)
                                        }
                                    },
                                    onLoadDetails: {
                                        viewModel.loadPokemonDetails(id: pokemon.id)
                                    }
                                )
                            }
                        }
                        .padding(.horizontal, 16)
                        .onAppear {
                            print("🖥️ Pokemon grid appeared with \(viewModel.filteredPokemonList.count) Pokemon")
                        }
                    }
                }
            }
            .background(Color(hex: "F8F8F8"))
            .navigationBarHidden(true)
            .onAppear {
                if viewModel.pokemonList.isEmpty {
                    viewModel.loadPokemonList()
                }
            }
        }
    }
}

struct PokemonSelectionCard: View {
    let pokemon: PokemonListItem
    let pokemonData: PokemonDisplayData?
    let isSelected: Bool
    let onTap: () -> Void
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
            ZStack {
                if let pokemonData = pokemonData {
                    AsyncImage(url: URL(string: pokemonData.imageUrl)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                    } placeholder: {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "FFB000")))
                    }
                    .frame(width: 80, height: 80)
                } else {
                    ZStack {
                        RoundedRectangle(cornerRadius: 8)
                            .fill(Color.gray.opacity(0.2))
                            .frame(width: 80, height: 80)

                        Text("?")
                            .font(.title)
                            .foregroundColor(.gray)
                    }
                    .onAppear {
                        onLoadDetails()
                    }
                }

                // Selection indicator
                if isSelected {
                    VStack {
                        HStack {
                            Spacer()
                            ZStack {
                                Circle()
                                    .fill(Color(hex: "FFB000"))
                                    .frame(width: 24, height: 24)

                                Image(systemName: "checkmark")
                                    .font(.caption)
                                    .fontWeight(.bold)
                                    .foregroundColor(.white)
                            }
                        }
                        Spacer()
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
        .background(isSelected ? Color(hex: "FFF8E1") : Color.white)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 1)
        .onTapGesture {
            onTap()
        }
    }
}

#Preview {
    PokemonSelectionView(
        isPresented: .constant(true),
        onPokemonSelected: .constant({ _ in })
    )
}