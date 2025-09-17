package com.cuscatlan.pokemon.data.repository

import com.cuscatlan.pokemon.data.api.PokemonApi
import com.cuscatlan.pokemon.data.model.Pokemon
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.data.model.PokemonListItem
import com.cuscatlan.pokemon.data.model.PokemonSpecies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
class PokemonRepository(
    private val api: PokemonApi
) {
    private var cachedPokemonList: List<PokemonListItem> = emptyList()
    private val cachedPokemonDetails = mutableMapOf<Int, PokemonDisplayData>()
    
    suspend fun getPokemonList(): Result<List<PokemonListItem>> {
        return try {
            if (cachedPokemonList.isEmpty()) {
                val response = api.getPokemonList(limit = 151, offset = 0)
                cachedPokemonList = response.results
            }
            Result.success(cachedPokemonList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun getPokemonDetails(id: Int): Result<PokemonDisplayData> {
        return try {
            if (cachedPokemonDetails.containsKey(id)) {
                Result.success(cachedPokemonDetails[id]!!)
            } else {
                val pokemon = api.getPokemon(id)
                val displayData = PokemonDisplayData.fromPokemon(pokemon)
                cachedPokemonDetails[id] = displayData
                Result.success(displayData)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPokemon(id: Int): Pokemon {
        return api.getPokemon(id)
    }

    suspend fun getPokemonSpecies(id: Int): PokemonSpecies {
        return api.getPokemonSpecies(id)
    }
    
    suspend fun searchPokemon(query: String): Result<List<PokemonListItem>> {
        return try {
            val allPokemon = if (cachedPokemonList.isEmpty()) {
                val response = api.getPokemonList(limit = 151, offset = 0)
                cachedPokemonList = response.results
                response.results
            } else {
                cachedPokemonList
            }
            
            val filtered = if (query.isBlank()) {
                allPokemon
            } else {
                allPokemon.filter { pokemon ->
                    pokemon.name.contains(query, ignoreCase = true) || 
                    pokemon.id.toString() == query
                }
            }
            
            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun getPokemonFlow(query: String = ""): Flow<Result<List<PokemonListItem>>> = flow {
        emit(searchPokemon(query))
    }
}