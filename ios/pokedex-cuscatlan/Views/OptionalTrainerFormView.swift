//
//  OptionalTrainerFormView.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import SwiftUI
import PhotosUI

struct OptionalTrainerFormView: View {
    @StateObject private var viewModel = OptionalTrainerFormViewModel()
    @State private var selectedPhoto: PhotosPickerItem?
    @State private var showingImagePicker = false
    @State private var showingDatePicker = false
    @State private var showingPokemonSelection = false
    @Binding var isPresented: Bool
    @Binding var onTrainerSaved: (Trainer) -> Void

    var body: some View {
        NavigationView {
            ScrollView {
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
                            Text("Configura perfil de entrenador")
                                .font(.title2)
                                .fontWeight(.bold)
                                .foregroundColor(.black)
                        }

                        Spacer()
                    }
                    .padding(.horizontal, 16)
                    .padding(.top, 20)

                    Text("Completa tu información personal")
                        .font(.body)
                        .foregroundColor(.gray)
                        .frame(maxWidth: .infinity, alignment: .leading)
                        .padding(.horizontal, 16)

                    // Photo Section - Card style similar to Pokémon cards
                    VStack(spacing: 16) {
                        Text("Foto de perfil")
                            .font(.body)
                            .fontWeight(.medium)
                            .foregroundColor(.black)

                        ZStack {
                            Circle()
                                .fill(Color.gray.opacity(0.2))
                                .frame(width: 100, height: 100)

                            if let photoUri = viewModel.formState.photoUri,
                               let url = URL(string: photoUri) {
                                AsyncImage(url: url) { image in
                                    image
                                        .resizable()
                                        .aspectRatio(contentMode: .fill)
                                        .frame(width: 100, height: 100)
                                        .clipShape(Circle())
                                } placeholder: {
                                    Image(systemName: "person.circle")
                                        .font(.system(size: 60))
                                        .foregroundColor(.gray)
                                }
                            } else {
                                Image(systemName: "person.circle")
                                    .font(.system(size: 60))
                                    .foregroundColor(.gray)
                            }
                        }

                        PhotosPicker(
                            selection: $selectedPhoto,
                            matching: .images
                        ) {
                            Text(viewModel.formState.photoUri != nil ? "Cambiar" : "Seleccionar")
                                .foregroundColor(Color(hex: "FFB000"))
                        }

                        Text("Opcional")
                            .font(.caption)
                            .foregroundColor(.gray)
                    }
                    .padding(20)
                    .background(Color.white)
                    .cornerRadius(16)
                    .shadow(color: .black.opacity(0.1), radius: 2, x: 0, y: 1)
                    .padding(.horizontal, 16)

                    // Name Field
                    VStack(alignment: .leading, spacing: 8) {
                        TextField("Ingresa tu nombre", text: Binding(
                            get: { viewModel.formState.name },
                            set: { viewModel.updateName($0) }
                        ))
                        .padding(.vertical, 16)
                        .padding(.horizontal, 20)
                        .background(Color.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(
                                    viewModel.validationErrors["name"] != nil ? Color.red :
                                    viewModel.formState.name.isEmpty ? Color.gray.opacity(0.3) : Color(hex: "FFB000"),
                                    lineWidth: 1
                                )
                        )
                        .cornerRadius(25)

                        Text("Nombre *")
                            .font(.caption)
                            .foregroundColor(.gray)

                        if let error = viewModel.validationErrors["name"] {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }
                    }
                    .padding(.horizontal, 16)

                    // Hobby Field
                    VStack(alignment: .leading, spacing: 8) {
                        TextField("Ej: leer, videojuegos, deportes", text: Binding(
                            get: { viewModel.formState.hobby },
                            set: { viewModel.updateHobby($0) }
                        ))
                        .padding(.vertical, 16)
                        .padding(.horizontal, 20)
                        .background(Color.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(Color(hex: "FFB000"), lineWidth: 1)
                        )
                        .cornerRadius(25)

                        Text("Pasatiempo favorito")
                            .font(.caption)
                            .foregroundColor(.gray)
                    }
                    .padding(.horizontal, 16)

                    // Birth Date Field
                    VStack(alignment: .leading, spacing: 8) {
                        HStack {
                            Text(viewModel.formState.birthDate?.formatted(date: .abbreviated, time: .omitted) ?? "DD/MM/AAAA")
                                .foregroundColor(viewModel.formState.birthDate == nil ? .gray : .black)

                            Spacer()

                            Button("Seleccionar") {
                                showingDatePicker = true
                            }
                            .foregroundColor(Color(hex: "FFB000"))
                        }
                        .padding()
                        .background(Color.white)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(
                                    viewModel.validationErrors["birthDate"] != nil ? Color.red :
                                    viewModel.formState.birthDate == nil ? Color.gray.opacity(0.3) : Color(hex: "FFB000"),
                                    lineWidth: 1
                                )
                        )
                        .cornerRadius(25)

                        Text("Fecha de nacimiento *")
                            .font(.caption)
                            .foregroundColor(.gray)

                        if let error = viewModel.validationErrors["birthDate"] {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        }

                        // Age Display
                        if let age = viewModel.formState.birthDate.map({ Calendar.current.dateComponents([.year], from: $0, to: Date()).year ?? 0 }) {
                            Text("Edad: \(age) años")
                                .font(.caption)
                                .foregroundColor(.gray)
                        }
                    }
                    .padding(.horizontal, 16)

                    // Identification Number Field
                    VStack(alignment: .leading, spacing: 8) {
                        let isAdult = viewModel.formState.birthDate.map {
                            Calendar.current.dateComponents([.year], from: $0, to: Date()).year ?? 0 >= 18
                        } ?? false

                        TextField(
                            isAdult ? "Ej: 12345678" : "Número de carnet",
                            text: Binding(
                                get: { viewModel.formState.identificationNumber },
                                set: { input in
                                    if isAdult {
                                        // Only allow numbers for DUI
                                        let formatted = DUIValidation.formatDUI(input)
                                        viewModel.updateIdentificationNumber(formatted)
                                    } else {
                                        // For minors, allow alphanumeric input
                                        viewModel.updateIdentificationNumber(input)
                                    }
                                }
                            )
                        )
                        .padding(.vertical, 16)
                        .padding(.horizontal, 20)
                        .background(Color.white)
                        .keyboardType(isAdult ? .numberPad : .default)
                        .overlay(
                            RoundedRectangle(cornerRadius: 25)
                                .stroke(
                                    viewModel.validationErrors["identificationNumber"] != nil ? Color.red :
                                    viewModel.formState.identificationNumber.isEmpty ? Color.gray.opacity(0.3) : Color(hex: "FFB000"),
                                    lineWidth: 1
                                )
                        )
                        .cornerRadius(25)

                        Text(isAdult ? "DUI *" : "Carnet de minoridad")
                            .font(.caption)
                            .foregroundColor(.gray)

                        if let error = viewModel.validationErrors["identificationNumber"] {
                            Text(error)
                                .font(.caption)
                                .foregroundColor(.red)
                        } else if isAdult {
                            Text("Solo números")
                                .font(.caption)
                                .foregroundColor(.gray)
                        } else {
                            Text("Campo opcional para menores de edad")
                                .font(.caption)
                                .foregroundColor(.gray)
                        }
                    }
                    .padding(.horizontal, 16)

                    // Continue Button
                    Button(action: {
                        if viewModel.isFormValid {
                            showingPokemonSelection = true
                        }
                    }) {
                        Text("Siguiente: Selecciona tu equipo Pokémon")
                            .font(.body)
                            .fontWeight(.medium)
                            .foregroundColor(.white)
                            .frame(maxWidth: .infinity)
                            .padding()
                            .background(viewModel.isFormValid ? Color(hex: "FFB000") : Color.gray)
                            .cornerRadius(25)
                    }
                    .disabled(!viewModel.isFormValid)
                    .padding(.horizontal, 16)
                    .padding(.top, 16)
                }
                .padding(.bottom, 32)
            }
            .background(Color(hex: "F8F8F8"))
            .navigationBarHidden(true)
        }
        .sheet(isPresented: $showingDatePicker) {
            DatePicker(
                "Fecha de nacimiento",
                selection: Binding(
                    get: { viewModel.formState.birthDate ?? Date() },
                    set: { viewModel.updateBirthDate($0) }
                ),
                in: ...Date(),
                displayedComponents: .date
            )
            .datePickerStyle(WheelDatePickerStyle())
            .presentationDetents([.medium])
        }
        .sheet(isPresented: $showingPokemonSelection) {
            PokemonSelectionView(
                isPresented: $showingPokemonSelection,
                onPokemonSelected: Binding(
                    get: { { selectedPokemon in
                        viewModel.updateSelectedPokemon(selectedPokemon)
                        viewModel.saveTrainer()
                        if let trainer = viewModel.trainer {
                            onTrainerSaved(trainer)
                            isPresented = false
                        }
                    }},
                    set: { _ in }
                )
            )
        }
        .onChange(of: selectedPhoto) { newItem in
            Task {
                if let data = try? await newItem?.loadTransferable(type: Data.self),
                   let uiImage = UIImage(data: data) {
                    // In a real app, you would save this to documents directory
                    // For now, we'll just use a placeholder string
                    viewModel.updatePhotoUri("local://photo")
                }
            }
        }
    }
}

#Preview {
    OptionalTrainerFormView(
        isPresented: .constant(true),
        onTrainerSaved: .constant({ _ in })
    )
}