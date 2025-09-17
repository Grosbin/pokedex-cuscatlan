package com.cuscatlan.pokemon.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.data.model.PokemonListItem
import com.cuscatlan.pokemon.data.repository.PokemonRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PokemonListViewModel(
    private val repository: PokemonRepository
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _pokemonList = MutableStateFlow<List<PokemonListItem>>(emptyList())
    val pokemonList: StateFlow<List<PokemonListItem>> = _pokemonList.asStateFlow()
    
    private val _pokemonDetails = MutableStateFlow<Map<Int, PokemonDisplayData>>(emptyMap())
    val pokemonDetails: StateFlow<Map<Int, PokemonDisplayData>> = _pokemonDetails.asStateFlow()
    
    private val _selectedPokemon = MutableStateFlow<Set<Int>>(emptySet())
    val selectedPokemon: StateFlow<Set<Int>> = _selectedPokemon.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _loadingPokemonIds = MutableStateFlow<Set<Int>>(emptySet())
    val loadingPokemonIds: StateFlow<Set<Int>> = _loadingPokemonIds.asStateFlow()
    
    val filteredPokemonList: StateFlow<List<PokemonListItem>> = combine(
        _pokemonList,
        _searchQuery
    ) { list, query ->
        if (query.isBlank()) {
            list
        } else {
            list.filter { pokemon ->
                pokemon.name.contains(query, ignoreCase = true) || 
                pokemon.id.toString().contains(query)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    val selectedPokemonData: StateFlow<List<PokemonDisplayData>> = combine(
        _selectedPokemon,
        _pokemonDetails
    ) { selected, details ->
        selected.mapNotNull { id -> details[id] }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    init {
        loadPokemonList()
    }
    
    private fun loadPokemonList() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            repository.getPokemonList()
                .onSuccess { pokemonList ->
                    _pokemonList.value = pokemonList
                }
                .onFailure { throwable ->
                    _error.value = "Failed to load Pokémon list: ${throwable.message}"
                }
            
            _isLoading.value = false
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun loadPokemonDetails(pokemonId: Int) {
        if (_pokemonDetails.value.containsKey(pokemonId) || 
            _loadingPokemonIds.value.contains(pokemonId)) {
            return
        }
        
        viewModelScope.launch {
            _loadingPokemonIds.value = _loadingPokemonIds.value + pokemonId
            
            repository.getPokemonDetails(pokemonId)
                .onSuccess { pokemonData ->
                    _pokemonDetails.value = _pokemonDetails.value + (pokemonId to pokemonData)
                }
                .onFailure { throwable ->
                    _error.value = "Failed to load Pokémon details: ${throwable.message}"
                }
            
            _loadingPokemonIds.value = _loadingPokemonIds.value - pokemonId
        }
    }
    
    fun togglePokemonSelection(pokemonId: Int) {
        val currentSelection = _selectedPokemon.value
        val newSelection = if (currentSelection.contains(pokemonId)) {
            currentSelection - pokemonId
        } else {
            if (currentSelection.size < 3) {
                currentSelection + pokemonId
            } else {
                currentSelection // Don't add if already at limit
            }
        }
        _selectedPokemon.value = newSelection
    }
    
    fun clearSelection() {
        _selectedPokemon.value = emptySet()
    }
    
    fun clearError() {
        _error.value = null
    }
    
    fun retry() {
        loadPokemonList()
    }
    
    fun setSelectedPokemon(pokemonIds: Set<Int>) {
        _selectedPokemon.value = pokemonIds
    }
}