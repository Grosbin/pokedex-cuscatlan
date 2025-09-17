//
//  PokemonListViewModel.swift
//  pokedex-cuscatlan
//
//  Created by User on 16/9/25.
//

import Foundation
import Combine

class PokemonListViewModel: ObservableObject {
    @Published var pokemonList: [PokemonListItem] = []
    @Published var pokemonDetails: [Int: PokemonDisplayData] = [:]
    @Published var filteredPokemonList: [PokemonListItem] = []
    @Published var searchQuery: String = "" {
        didSet {
            filterPokemon()
        }
    }
    @Published var isLoading = false
    @Published var error: String?
    @Published var selectedPokemon: Set<Int> = []
    @Published var selectedPokemonData: [PokemonDisplayData] = []

    private let repository = PokemonRepository.shared
    private var cancellables = Set<AnyCancellable>()

    init() {
        loadPokemonList()
    }

    func loadPokemonList() {
        isLoading = true
        error = nil
        print("🔄 Loading Pokemon list...")

        repository.fetchPokemonList()
            .sink(
                receiveCompletion: { [weak self] completion in
                    DispatchQueue.main.async {
                        self?.isLoading = false
                        if case .failure(let error) = completion {
                            print("❌ Error loading Pokemon list: \(error.localizedDescription)")
                            self?.error = "Error loading Pokémon: \(error.localizedDescription)"
                        } else {
                            print("✅ Pokemon list loaded successfully")
                        }
                    }
                },
                receiveValue: { [weak self] pokemonList in
                    DispatchQueue.main.async {
                        print("📝 Received \(pokemonList.count) Pokemon")
                        self?.pokemonList = pokemonList
                        self?.filteredPokemonList = pokemonList
                    }
                }
            )
            .store(in: &cancellables)
    }

    func loadPokemonDetails(id: Int) {
        guard pokemonDetails[id] == nil else { return }

        repository.fetchPokemonDetail(id: id)
            .sink(
                receiveCompletion: { [weak self] completion in
                    if case .failure(let error) = completion {
                        DispatchQueue.main.async {
                            self?.error = "Error loading Pokémon details: \(error.localizedDescription)"
                        }
                    }
                },
                receiveValue: { [weak self] pokemonData in
                    DispatchQueue.main.async {
                        self?.pokemonDetails[id] = pokemonData
                    }
                }
            )
            .store(in: &cancellables)
    }

    func togglePokemonSelection(_ id: Int) {
        if selectedPokemon.contains(id) {
            selectedPokemon.remove(id)
        } else if selectedPokemon.count < 3 {
            selectedPokemon.insert(id)
        }
        updateSelectedPokemonData()
    }

    func clearSelection() {
        selectedPokemon.removeAll()
        updateSelectedPokemonData()
    }

    private func updateSelectedPokemonData() {
        selectedPokemonData = selectedPokemon.compactMap { pokemonDetails[$0] }
    }

    private func filterPokemon() {
        if searchQuery.isEmpty {
            filteredPokemonList = pokemonList
        } else {
            filteredPokemonList = pokemonList.filter { pokemon in
                pokemon.name.lowercased().contains(searchQuery.lowercased()) ||
                String(pokemon.id).contains(searchQuery)
            }
        }
    }

    func updateSearchQuery(_ query: String) {
        searchQuery = query
    }

    func clearError() {
        error = nil
    }

    func retry() {
        loadPokemonList()
    }
}