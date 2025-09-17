//
//  PokemonModels.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation

// MARK: - Pokemon List Response
struct PokemonListResponse: Codable {
    let results: [PokemonListItem]
}

struct PokemonListItem: Codable, Identifiable {
    let name: String
    let url: String

    var id: Int {
        let components = url.components(separatedBy: "/")
        return Int(components[components.count - 2]) ?? 0
    }
}

// MARK: - Pokemon Detail Response
struct PokemonDetailResponse: Codable {
    let id: Int
    let name: String
    let height: Int
    let weight: Int
    let types: [PokemonTypeSlot]
    let sprites: PokemonSprites
    let stats: [PokemonStat]
    let species: PokemonSpecies
}

struct PokemonTypeSlot: Codable {
    let type: PokemonType
}

struct PokemonType: Codable {
    let name: String
}

struct PokemonSprites: Codable {
    let other: PokemonSpritesOther
}

struct PokemonSpritesOther: Codable {
    let officialArtwork: PokemonOfficialArtwork

    private enum CodingKeys: String, CodingKey {
        case officialArtwork = "official-artwork"
    }
}

struct PokemonOfficialArtwork: Codable {
    let frontDefault: String?

    private enum CodingKeys: String, CodingKey {
        case frontDefault = "front_default"
    }
}

struct PokemonStat: Codable {
    let baseStat: Int
    let stat: StatInfo

    private enum CodingKeys: String, CodingKey {
        case baseStat = "base_stat"
        case stat
    }
}

struct StatInfo: Codable {
    let name: String
}

struct PokemonSpecies: Codable {
    let url: String
}

// MARK: - Pokemon Species Response
struct PokemonSpeciesResponse: Codable {
    let flavorTextEntries: [FlavorTextEntry]

    private enum CodingKeys: String, CodingKey {
        case flavorTextEntries = "flavor_text_entries"
    }
}

struct FlavorTextEntry: Codable {
    let flavorText: String
    let language: Language

    private enum CodingKeys: String, CodingKey {
        case flavorText = "flavor_text"
        case language
    }
}

struct Language: Codable {
    let name: String
}

// MARK: - Display Models
struct PokemonDisplayData: Identifiable, Codable {
    let id: Int
    let name: String
    let imageUrl: String
    let types: [String]
    let height: Int
    let weight: Int
    let stats: [String: Int]
    let description: String
}

// MARK: - Trainer Models
struct Trainer: Identifiable, Codable {
    let id: UUID
    let name: String
    let photoUri: String?
    let hobby: String?
    let birthDate: Date
    let identificationNumber: String?
    let selectedPokemon: [PokemonDisplayData]

    var age: Int {
        Calendar.current.dateComponents([.year], from: birthDate, to: Date()).year ?? 0
    }

    var isAdult: Bool {
        age >= 18
    }

    init(name: String, photoUri: String?, hobby: String?, birthDate: Date, identificationNumber: String?, selectedPokemon: [PokemonDisplayData]) {
        self.id = UUID()
        self.name = name
        self.photoUri = photoUri
        self.hobby = hobby
        self.birthDate = birthDate
        self.identificationNumber = identificationNumber
        self.selectedPokemon = selectedPokemon
    }

    // Custom init for decoding
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        self.id = try container.decode(UUID.self, forKey: .id)
        self.name = try container.decode(String.self, forKey: .name)
        self.photoUri = try container.decodeIfPresent(String.self, forKey: .photoUri)
        self.hobby = try container.decodeIfPresent(String.self, forKey: .hobby)
        self.birthDate = try container.decode(Date.self, forKey: .birthDate)
        self.identificationNumber = try container.decodeIfPresent(String.self, forKey: .identificationNumber)
        self.selectedPokemon = try container.decode([PokemonDisplayData].self, forKey: .selectedPokemon)
    }

    private enum CodingKeys: String, CodingKey {
        case id, name, photoUri, hobby, birthDate, identificationNumber, selectedPokemon
    }
}

struct TrainerFormData {
    var name: String = ""
    var photoUri: String? = nil
    var hobby: String = ""
    var birthDate: Date? = nil
    var identificationNumber: String = ""
    var selectedPokemon: [PokemonDisplayData] = []

    func toTrainer() -> Trainer {
        return Trainer(
            name: name,
            photoUri: photoUri,
            hobby: hobby.isEmpty ? nil : hobby,
            birthDate: birthDate ?? Date(),
            identificationNumber: identificationNumber.isEmpty ? nil : identificationNumber,
            selectedPokemon: selectedPokemon
        )
    }
}