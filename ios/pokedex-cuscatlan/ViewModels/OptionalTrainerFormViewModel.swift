//
//  OptionalTrainerFormViewModel.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation
import Combine

class OptionalTrainerFormViewModel: ObservableObject {
    @Published var formState = TrainerFormData()
    @Published var validationErrors: [String: String] = [:]
    @Published var trainer: Trainer?
    @Published var isFormValid = false

    private var cancellables = Set<AnyCancellable>()

    init() {
        // Observe form changes to update validation
        Publishers.CombineLatest4(
            $formState.map(\.name),
            $formState.map(\.birthDate),
            $formState.map(\.identificationNumber),
            $validationErrors
        )
        .map { _, _, _, _ in self.checkFormValidity() }
        .assign(to: &$isFormValid)
    }

    func updateName(_ name: String) {
        formState.name = name
        validateName(name)
    }

    func updatePhotoUri(_ uri: String?) {
        formState.photoUri = uri
    }

    func updateHobby(_ hobby: String) {
        formState.hobby = hobby
    }

    func updateBirthDate(_ date: Date?) {
        formState.birthDate = date
        validateBirthDate(date)

        // Revalidate identification when birth date changes
        if !formState.identificationNumber.isEmpty {
            validateIdentification(formState.identificationNumber)
        }
    }

    func updateIdentificationNumber(_ number: String) {
        formState.identificationNumber = number
        validateIdentification(number)
    }

    func updateSelectedPokemon(_ pokemon: [PokemonDisplayData]) {
        formState.selectedPokemon = pokemon
    }

    private func validateName(_ name: String) {
        if name.trimmingCharacters(in: .whitespaces).isEmpty {
            validationErrors["name"] = "El nombre es requerido"
        } else if name.trimmingCharacters(in: .whitespaces).count < 2 {
            validationErrors["name"] = "El nombre debe tener al menos 2 caracteres"
        } else {
            validationErrors.removeValue(forKey: "name")
        }
    }

    private func validateBirthDate(_ date: Date?) {
        if date == nil {
            validationErrors["birthDate"] = "La fecha de nacimiento es requerida"
        } else if let date = date, date > Date() {
            validationErrors["birthDate"] = "La fecha de nacimiento no puede ser futura"
        } else {
            validationErrors.removeValue(forKey: "birthDate")
        }
    }

    private func validateIdentification(_ identification: String) {
        guard let birthDate = formState.birthDate else { return }

        let age = Calendar.current.dateComponents([.year], from: birthDate, to: Date()).year ?? 0
        let isAdult = age >= 18

        if isAdult {
            // Adult - DUI is required and must be valid
            if identification.trimmingCharacters(in: .whitespaces).isEmpty {
                validationErrors["identificationNumber"] = "El DUI es requerido para mayores de edad"
            } else if !DUIValidation.isValidDUI(identification) {
                validationErrors["identificationNumber"] = "Formato de DUI inválido"
            } else {
                validationErrors.removeValue(forKey: "identificationNumber")
            }
        } else {
            // Minor - carnet is optional but if provided, must be valid
            if !identification.trimmingCharacters(in: .whitespaces).isEmpty &&
               !CarnetValidation.isValidCarnet(identification) {
                validationErrors["identificationNumber"] = "Formato de carnet inválido"
            } else {
                validationErrors.removeValue(forKey: "identificationNumber")
            }
        }
    }

    private func checkFormValidity() -> Bool {
        let hasName = !formState.name.trimmingCharacters(in: .whitespaces).isEmpty &&
                     formState.name.trimmingCharacters(in: .whitespaces).count >= 2
        let hasBirthDate = formState.birthDate != nil && formState.birthDate! <= Date()

        let hasValidIdentification: Bool
        if let birthDate = formState.birthDate {
            let age = Calendar.current.dateComponents([.year], from: birthDate, to: Date()).year ?? 0
            let isAdult = age >= 18

            if isAdult {
                // Adult - DUI required and valid
                hasValidIdentification = !formState.identificationNumber.trimmingCharacters(in: .whitespaces).isEmpty &&
                                       DUIValidation.isValidDUI(formState.identificationNumber)
            } else {
                // Minor - carnet optional, but if provided must be valid
                hasValidIdentification = formState.identificationNumber.trimmingCharacters(in: .whitespaces).isEmpty ||
                                       CarnetValidation.isValidCarnet(formState.identificationNumber)
            }
        } else {
            hasValidIdentification = false
        }

        return hasName && hasBirthDate && hasValidIdentification && validationErrors.isEmpty
    }

    func saveTrainer() {
        if isFormValid {
            trainer = formState.toTrainer()
        }
    }

    func loadTrainer(_ trainer: Trainer) {
        self.trainer = trainer
        self.formState = TrainerFormData()
        self.formState.name = trainer.name
        self.formState.photoUri = trainer.photoUri
        self.formState.hobby = trainer.hobby ?? ""
        self.formState.birthDate = trainer.birthDate
        self.formState.identificationNumber = trainer.identificationNumber ?? ""
        self.formState.selectedPokemon = trainer.selectedPokemon
    }

    func clearForm() {
        formState = TrainerFormData()
        validationErrors.removeAll()
        trainer = nil
    }
}