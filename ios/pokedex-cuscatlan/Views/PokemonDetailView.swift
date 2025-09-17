//
//  PokemonDetailView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

struct PokemonDetailView: View {
    let pokemonId: Int
    @StateObject private var viewModel = PokemonDetailViewModel()
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        ScrollView {
            VStack(spacing: 0) {
                if let pokemonData = viewModel.pokemonData {
                    headerSection(for: pokemonData)
                    contentSection(for: pokemonData)

                } else if viewModel.isLoading {
                    VStack {
                        Spacer()
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "3B82F6")))
                            .scaleEffect(1.5)
                        Text("Cargando Pokémon...")
                            .foregroundColor(.black)
                            .padding(.top, 16)
                        Spacer()
                    }
                } else if let error = viewModel.error {
                    VStack {
                        Text("Error: \(error)")
                            .foregroundColor(.red)
                        Button("Reintentar") {
                            viewModel.loadPokemonDetail(id: pokemonId)
                        }
                        .foregroundColor(Color(hex: "FFB000"))
                    }
                    .padding()
                }
            }
        }
        .background(Color(hex: "F8F8F8"))
        .navigationBarTitleDisplayMode(.inline)
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button(action: {
                    dismiss()
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
            }
        }
        .onAppear {
            print("🔍 PokemonDetailView appeared for ID: \(pokemonId)")
            viewModel.loadPokemonDetail(id: pokemonId)
        }
        .onDisappear {
            print("👋 PokemonDetailView disappeared for ID: \(pokemonId)")
        }
    }

    // MARK: - Header Section
    @ViewBuilder
    private func headerSection(for pokemonData: PokemonDisplayData) -> some View {
        ZStack {
            let primaryType = pokemonData.types.first ?? "normal"
            let typeColor = Color.pokemonTypeColor(for: primaryType)

            LinearGradient(
                gradient: Gradient(colors: [typeColor, typeColor.opacity(0.6)]),
                startPoint: .topLeading,
                endPoint: .bottomTrailing
            )
            .frame(height: 300)

            VStack {
                AsyncImage(url: URL(string: pokemonData.imageUrl)) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                } placeholder: {
                    ProgressView()
                        .progressViewStyle(CircularProgressViewStyle(tint: .white))
                        .scaleEffect(1.5)
                }
                .frame(width: 200, height: 200)
                .padding(.top, 40)
            }
        }
    }

    // MARK: - Content Section
    @ViewBuilder
    private func contentSection(for pokemonData: PokemonDisplayData) -> some View {
        VStack(alignment: .leading, spacing: 20) {
            nameAndTypesSection(for: pokemonData)
            physicalInfoSection(for: pokemonData)
            descriptionSection(for: pokemonData)
            statsSection(for: pokemonData)
        }
        .padding(20)
        .background(Color.white)
        .cornerRadius(20)
        .offset(y: -20)
    }

    // MARK: - Name and Types Section
    @ViewBuilder
    private func nameAndTypesSection(for pokemonData: PokemonDisplayData) -> some View {
        HStack {
            VStack(alignment: .leading) {
                Text("#\(String(format: "%03d", pokemonData.id))")
                    .font(.caption)
                    .foregroundColor(.gray)

                Text(pokemonData.name)
                    .font(.largeTitle)
                    .fontWeight(.bold)
                    .foregroundColor(.black)
            }

            Spacer()

            HStack(spacing: 8) {
                ForEach(pokemonData.types, id: \.self) { type in
                    Text(type.capitalized)
                        .font(.caption)
                        .fontWeight(.medium)
                        .foregroundColor(.black)
                        .padding(.horizontal, 12)
                        .padding(.vertical, 4)
                        .background(Color.white.opacity(0.9))
                        .cornerRadius(12)
                }
            }
        }
    }

    // MARK: - Physical Info Section
    @ViewBuilder
    private func physicalInfoSection(for pokemonData: PokemonDisplayData) -> some View {
        HStack(spacing: 20) {
            let heightInMeters = Double(pokemonData.height) / 10
            let weightInKg = Double(pokemonData.weight) / 10

            InfoCard(
                title: "Altura",
                subtitle: String(format: "%.1f m", heightInMeters),
                icon: "⚖"
            )

            InfoCard(
                title: "Peso",
                subtitle: String(format: "%.1f kg", weightInKg),
                icon: "📏"
            )
        }
    }

    // MARK: - Description Section
    @ViewBuilder
    private func descriptionSection(for pokemonData: PokemonDisplayData) -> some View {
        Text("Descripción")
            .font(.title2)
            .fontWeight(.bold)
            .foregroundColor(.black)

        Text(pokemonData.description)
            .font(.body)
            .foregroundColor(.gray)
            .lineLimit(nil)
    }

    // MARK: - Stats Section
    @ViewBuilder
    private func statsSection(for pokemonData: PokemonDisplayData) -> some View {
        Text("Estadísticas")
            .font(.title2)
            .fontWeight(.bold)
            .foregroundColor(.black)

        VStack(spacing: 8) {
            let sortedStats = Array(pokemonData.stats.keys.sorted())
            let primaryTypeColor = Color.pokemonTypeColor(for: pokemonData.types.first ?? "normal")

            ForEach(sortedStats, id: \.self) { statName in
                if let value = pokemonData.stats[statName] {
                    StatRow(
                        statName: statName,
                        value: value,
                        maxValue: 150,
                        color: primaryTypeColor
                    )
                }
            }
        }
    }
}

struct InfoCard: View {
    let title: String
    let subtitle: String
    let icon: String

    var body: some View {
        VStack {
            Text(icon)
                .font(.title)

            Text(subtitle)
                .font(.title3)
                .fontWeight(.bold)
                .foregroundColor(.black)

            Text(title)
                .font(.caption)
                .foregroundColor(.gray)
        }
        .frame(maxWidth: .infinity)
        .padding()
        .background(Color(hex: "F8F9FA"))
        .cornerRadius(12)
    }
}

struct StatRow: View {
    let statName: String
    let value: Int
    let maxValue: Int
    let color: Color

    var body: some View {
        HStack {
            Text(statName)
                .font(.body)
                .foregroundColor(.black)
                .frame(width: 100, alignment: .leading)

            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 10)
                    .fill(Color.gray.opacity(0.3))
                    .frame(height: 20)

                RoundedRectangle(cornerRadius: 10)
                    .fill(color)
                    .frame(width: CGFloat(value) / CGFloat(maxValue) * 200, height: 20)
            }
            .frame(maxWidth: .infinity)

            Text(String(format: "%03d", value))
                .font(.body)
                .fontWeight(.bold)
                .foregroundColor(.black)
                .frame(width: 40, alignment: .trailing)
        }
    }
}

#Preview {
    NavigationView {
        PokemonDetailView(pokemonId: 1)
    }
}