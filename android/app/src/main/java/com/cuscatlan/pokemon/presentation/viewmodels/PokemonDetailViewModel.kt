package com.cuscatlan.pokemon.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.data.repository.PokemonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PokemonDetailViewModel(
    private val repository: PokemonRepository,
    private val pokemonId: Int
) : ViewModel() {

    private val _pokemonData = MutableStateFlow<PokemonDisplayData?>(null)
    val pokemonData: StateFlow<PokemonDisplayData?> = _pokemonData.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun loadPokemonDetail(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val pokemon = repository.getPokemon(id)
                val species = try {
                    repository.getPokemonSpecies(id)
                } catch (e: Exception) {
                    null
                }

                _pokemonData.value = PokemonDisplayData.fromPokemon(pokemon, species)
            } catch (e: Exception) {
                _error.value = "Error loading Pokemon details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}