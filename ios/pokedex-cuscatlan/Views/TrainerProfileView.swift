//
//  TrainerProfileView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI

struct TrainerProfileView: View {
    let trainer: Trainer
    @Binding var isPresented: Bool
    @Binding var onEditProfile: () -> Void
    @Binding var onEditPokemon: () -> Void

    var body: some View {
        NavigationView {
            ScrollView {
                VStack(spacing: 16) {
                    // Header
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

                        Text("Perfil del entrenador")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.black)

                        Spacer()

                        Button(action: onEditProfile) {
                            Image(systemName: "pencil")
                                .font(.title2)
                                .foregroundColor(Color(hex: "FFB000"))
                        }
                    }
                    .padding(.horizontal, 16)
                    .padding(.top, 20)

                    // Trainer Info Card
                    VStack(spacing: 20) {
                        // Photo
                        ZStack {
                            Circle()
                                .stroke(Color(hex: "FFB000"), lineWidth: 3)
                                .frame(width: 120, height: 120)

                            if let photoUri = trainer.photoUri,
                               let url = URL(string: photoUri) {
                                AsyncImage(url: url) { image in
                                    image
                                        .resizable()
                                        .aspectRatio(contentMode: .fill)
                                        .frame(width: 120, height: 120)
                                        .clipShape(Circle())
                                } placeholder: {
                                    Image(systemName: "person.circle")
                                        .font(.system(size: 80))
                                        .foregroundColor(.gray)
                                }
                            } else {
                                Image(systemName: "person.circle")
                                    .font(.system(size: 80))
                                    .foregroundColor(.gray)
                            }
                        }

                        // Name
                        Text(trainer.name)
                            .font(.title)
                            .fontWeight(.bold)
                            .foregroundColor(.black)

                        // Details
                        VStack(spacing: 16) {
                            if let hobby = trainer.hobby {
                                ProfileDetailCard(label: "Pasatiempo", value: hobby)
                            }

                            ProfileDetailCard(label: "Edad", value: "\(trainer.age) años")

                            if let id = trainer.identificationNumber, !id.isEmpty {
                                ProfileDetailCard(
                                    label: trainer.isAdult ? "DUI" : "Carnet de minoridad",
                                    value: id
                                )
                            }
                        }
                    }
                    .padding(24)
                    .background(Color.white)
                    .cornerRadius(16)
                    .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
                    .padding(.horizontal, 16)

                    // Pokemon Team Section
                    HStack {
                        Text("Mostrar equipo Pokémon")
                            .font(.title2)
                            .fontWeight(.bold)
                            .foregroundColor(.black)

                        Spacer()

                        Button("Editar equipo") {
                            onEditPokemon()
                        }
                        .foregroundColor(Color(hex: "FFB000"))
                    }
                    .padding(.horizontal, 16)

                    if trainer.selectedPokemon.isEmpty {
                        VStack {
                            Text("No hay Pokémon seleccionados aún")
                                .font(.body)
                                .foregroundColor(.gray)
                                .multilineTextAlignment(.center)
                                .padding(32)
                        }
                        .frame(maxWidth: .infinity)
                        .background(Color.white)
                        .cornerRadius(16)
                        .padding(.horizontal, 16)
                    } else {
                        VStack(spacing: 16) {
                            ForEach(trainer.selectedPokemon, id: \.id) { pokemon in
                                PokemonTeamCard(pokemon: pokemon)
                            }
                        }
                        .padding(.horizontal, 16)
                    }

                    Spacer(minLength: 32)
                }
                .padding(.bottom, 32)
            }
            .background(Color(hex: "F8F8F8"))
            .navigationBarHidden(true)
        }
    }
}

struct ProfileDetailCard: View {
    let label: String
    let value: String

    var body: some View {
        HStack {
            Text(label)
                .font(.body)
                .foregroundColor(.gray)
                .fontWeight(.medium)

            Spacer()

            Text(value)
                .font(.body)
                .fontWeight(.bold)
                .foregroundColor(.black)
        }
        .padding(16)
        .background(Color(hex: "F8F9FA"))
        .cornerRadius(8)
    }
}

struct PokemonTeamCard: View {
    let pokemon: PokemonDisplayData

    var body: some View {
        VStack(spacing: 16) {
            HStack(alignment: .center, spacing: 16) {
                // Pokemon Image (Sprite)
                ZStack {
                    RoundedRectangle(cornerRadius: 12)
                        .fill(Color.pokemonTypeColor(for: pokemon.types.first ?? "normal").opacity(0.1))
                        .frame(width: 80, height: 80)

                    AsyncImage(url: URL(string: pokemon.imageUrl)) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                            .frame(width: 64, height: 64)
                    } placeholder: {
                        ProgressView()
                            .progressViewStyle(CircularProgressViewStyle(tint: Color(hex: "FFB000")))
                    }
                }

                // Pokemon Info
                VStack(alignment: .leading, spacing: 4) {
                    Text(pokemon.name)
                        .font(.title3)
                        .fontWeight(.bold)
                        .foregroundColor(.black)

                    // Types
                    HStack(spacing: 8) {
                        ForEach(pokemon.types, id: \.self) { type in
                            TypeBadge(typeName: type)
                        }
                    }
                }

                Spacer()
            }

            // Stats with progress bars
            VStack(alignment: .leading, spacing: 12) {
                Text("Barra de progreso de stats")
                    .font(.body)
                    .fontWeight(.bold)
                    .foregroundColor(.black)

                VStack(spacing: 8) {
                    ForEach(Array(pokemon.stats.keys.sorted()), id: \.self) { statName in
                        if let value = pokemon.stats[statName] {
                            StatProgressBar(
                                statName: statName,
                                value: value,
                                maxValue: 150,
                                color: Color.pokemonTypeColor(for: pokemon.types.first ?? "normal")
                            )
                        }
                    }
                }
            }
        }
        .padding(16)
        .background(Color.white)
        .cornerRadius(16)
        .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 1)
    }
}

struct TypeBadge: View {
    let typeName: String

    var body: some View {
        Text(typeName.capitalized)
            .font(.caption)
            .fontWeight(.medium)
            .foregroundColor(.white)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
            .background(Color.pokemonTypeColor(for: typeName))
            .cornerRadius(12)
    }
}

struct StatProgressBar: View {
    let statName: String
    let value: Int
    let maxValue: Int
    let color: Color

    var body: some View {
        HStack {
            Text(statName)
                .font(.body)
                .foregroundColor(.black)
                .frame(width: 120, alignment: .leading)

            ZStack(alignment: .leading) {
                RoundedRectangle(cornerRadius: 4)
                    .fill(Color.gray.opacity(0.3))
                    .frame(height: 8)

                RoundedRectangle(cornerRadius: 4)
                    .fill(color)
                    .frame(width: CGFloat(value) / CGFloat(maxValue) * 150, height: 8)
            }
            .frame(maxWidth: .infinity)

            Text("\(value)")
                .font(.body)
                .fontWeight(.bold)
                .foregroundColor(.black)
                .frame(width: 40, alignment: .trailing)
        }
    }
}

#Preview {
    let sampleTrainer = Trainer(
        name: "Ash Ketchum",
        photoUri: nil,
        hobby: "Entrenar Pokémon",
        birthDate: Calendar.current.date(byAdding: .year, value: -20, to: Date()) ?? Date(),
        identificationNumber: "12345678-9",
        selectedPokemon: []
    )

    TrainerProfileView(
        trainer: sampleTrainer,
        isPresented: .constant(true),
        onEditProfile: .constant({}),
        onEditPokemon: .constant({})
    )
}