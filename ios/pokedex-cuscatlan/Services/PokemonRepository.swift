//
//  PokemonRepository.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation
import Combine

class PokemonRepository: ObservableObject {
    static let shared = PokemonRepository()

    private let baseURL = "https://pokeapi.co/api/v2"

    private init() {}

    func fetchPokemonList() -> AnyPublisher<[PokemonListItem], Error> {
        guard let url = URL(string: "\(baseURL)/pokemon?limit=151") else {
            return Fail(error: URLError(.badURL))
                .eraseToAnyPublisher()
        }

        return URLSession.shared.dataTaskPublisher(for: url)
            .map(\.data)
            .decode(type: PokemonListResponse.self, decoder: JSONDecoder())
            .map(\.results)
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }

    func fetchPokemonDetail(id: Int) -> AnyPublisher<PokemonDisplayData, Error> {
        guard let url = URL(string: "\(baseURL)/pokemon/\(id)") else {
            return Fail(error: URLError(.badURL))
                .eraseToAnyPublisher()
        }

        return URLSession.shared.dataTaskPublisher(for: url)
            .map(\.data)
            .decode(type: PokemonDetailResponse.self, decoder: JSONDecoder())
            .flatMap { detail in
                self.fetchPokemonDescription(speciesUrl: detail.species.url)
                    .map { description in
                        PokemonDisplayData(
                            id: detail.id,
                            name: detail.name.capitalized,
                            imageUrl: detail.sprites.other.officialArtwork.frontDefault ?? "",
                            types: detail.types.map { $0.type.name },
                            height: detail.height,
                            weight: detail.weight,
                            stats: Dictionary(uniqueKeysWithValues: detail.stats.map {
                                (self.formatStatName($0.stat.name), $0.baseStat)
                            }),
                            description: description
                        )
                    }
            }
            .receive(on: DispatchQueue.main)
            .eraseToAnyPublisher()
    }

    private func fetchPokemonDescription(speciesUrl: String) -> AnyPublisher<String, Error> {
        guard let url = URL(string: speciesUrl) else {
            return Just("No description available")
                .setFailureType(to: Error.self)
                .eraseToAnyPublisher()
        }

        return URLSession.shared.dataTaskPublisher(for: url)
            .map(\.data)
            .decode(type: PokemonSpeciesResponse.self, decoder: JSONDecoder())
            .map { species in
                let spanishEntry = species.flavorTextEntries.first { $0.language.name == "es" }
                let englishEntry = species.flavorTextEntries.first { $0.language.name == "en" }

                return (spanishEntry?.flavorText ?? englishEntry?.flavorText ?? "No description available")
                    .replacingOccurrences(of: "\n", with: " ")
                    .replacingOccurrences(of: "\u{0C}", with: " ")
            }
            .catch { _ in
                Just("No description available")
                    .setFailureType(to: Error.self)
            }
            .eraseToAnyPublisher()
    }

    private func formatStatName(_ statName: String) -> String {
        switch statName {
        case "hp": return "HP"
        case "attack": return "Ataque"
        case "defense": return "Defensa"
        case "special-attack": return "Ataque Esp."
        case "special-defense": return "Defensa Esp."
        case "speed": return "Velocidad"
        default: return statName.capitalized
        }
    }
}