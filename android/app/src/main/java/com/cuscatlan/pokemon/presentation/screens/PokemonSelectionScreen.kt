package com.cuscatlan.pokemon.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cuscatlan.pokemon.di.DependencyContainer
import com.cuscatlan.pokemon.presentation.components.PokemonCard
import com.cuscatlan.pokemon.presentation.viewmodels.PokemonListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonSelectionScreen(
    onNavigateNext: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = viewModel { PokemonListViewModel(DependencyContainer.pokemonRepository) }
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredPokemonList by viewModel.filteredPokemonList.collectAsStateWithLifecycle()
    val pokemonDetails by viewModel.pokemonDetails.collectAsStateWithLifecycle()
    val selectedPokemon by viewModel.selectedPokemon.collectAsStateWithLifecycle()
    val selectedPokemonData by viewModel.selectedPokemonData.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val loadingPokemonIds by viewModel.loadingPokemonIds.collectAsStateWithLifecycle()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
    ) {
        // Header with back button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        Color(0xFFFFB000).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(20.dp)
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Regresar",
                    tint = Color(0xFFFFB000),
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "Selecciona tu equipo Pokémon",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Text(
            text = "Elige 3 Pokémon de la primera generación",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Selection Counter
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFF8E1)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Seleccionados: ${selectedPokemon.size}/3",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFFFB000)
                )

                if (selectedPokemon.isNotEmpty()) {
                    TextButton(onClick = { viewModel.clearSelection() }) {
                        Text("Limpiar todo", color = Color(0xFFFFB000))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            label = { Text("Buscar Pokémon por nombre o ID") },
            placeholder = { Text("Ej: pikachu o 25") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFFFFB000)
                )
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "Clear search",
                            tint = Color.Gray
                        )
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(25.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color(0xFFFFB000),
                unfocusedBorderColor = Color.LightGray
            )
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Continue Button
        if (selectedPokemon.size == 3) {
            Button(
                onClick = onNavigateNext,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFB000)
                ),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text(
                    "Continuar con el equipo seleccionado",
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Error Display
        error?.let { errorMessage ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    
                    TextButton(
                        onClick = {
                            viewModel.clearError()
                            viewModel.retry()
                        }
                    ) {
                        Text("Reintentar", color = Color(0xFFFFB000))
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Loading State
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator(color = Color(0xFFFFB000))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Cargando Pokémon...", color = Color.Black)
                }
            }
        } else {
            // Pokemon Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPokemonList) { pokemon ->
                    PokemonCard(
                        pokemon = pokemon,
                        pokemonData = pokemonDetails[pokemon.id],
                        isSelected = selectedPokemon.contains(pokemon.id),
                        isLoading = loadingPokemonIds.contains(pokemon.id),
                        onClick = { pokemonId ->
                            if (selectedPokemon.size < 3 || selectedPokemon.contains(pokemonId)) {
                                viewModel.togglePokemonSelection(pokemonId)
                            }
                        },
                        onLoadDetails = viewModel::loadPokemonDetails
                    )
                }
            }
        }
    }
}