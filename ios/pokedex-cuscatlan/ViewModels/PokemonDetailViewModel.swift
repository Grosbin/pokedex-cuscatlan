//
//  PokemonDetailViewModel.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation
import Combine

class PokemonDetailViewModel: ObservableObject {
    @Published var pokemonData: PokemonDisplayData?
    @Published var isLoading = false
    @Published var error: String?

    private let repository = PokemonRepository.shared
    private var cancellables = Set<AnyCancellable>()

    func loadPokemonDetail(id: Int) {
        print("🚀 Loading Pokemon detail for ID: \(id)")
        isLoading = true
        error = nil

        repository.fetchPokemonDetail(id: id)
            .sink(
                receiveCompletion: { [weak self] completion in
                    DispatchQueue.main.async {
                        self?.isLoading = false
                        if case .failure(let error) = completion {
                            self?.error = error.localizedDescription
                        }
                    }
                },
                receiveValue: { [weak self] pokemonData in
                    DispatchQueue.main.async {
                        self?.pokemonData = pokemonData
                    }
                }
            )
            .store(in: &cancellables)
    }
}