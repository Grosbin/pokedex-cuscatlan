package com.cuscatlan.pokemon.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.di.DependencyContainer
import com.cuscatlan.pokemon.presentation.viewmodels.PokemonDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PokemonDetailViewModel = viewModel { PokemonDetailViewModel(DependencyContainer.pokemonRepository, pokemonId) }
) {
    val pokemonData by viewModel.pokemonData.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(pokemonId) {
        viewModel.loadPokemonDetail(pokemonId)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        // Header
        TopAppBar(
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = pokemonData?.name ?: "Loading...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    if (pokemonData != null) {
                        Text(
                            text = "#${pokemonData!!.id.toString().padStart(3, '0')}",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                    }
                }
            },
            navigationIcon = {
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
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFFFFB000),
                        modifier = Modifier.size(24.dp)
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFFF8F8F8)
            )
        )

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF3B82F6))
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = error!!,
                    color = Color.Red,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        } else if (pokemonData != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Pokemon Image Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = getPokemonTypeColor(pokemonData!!.types.firstOrNull() ?: "normal")
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = pokemonData!!.imageUrl,
                            contentDescription = pokemonData!!.name,
                            modifier = Modifier.size(150.dp),
                            contentScale = ContentScale.Fit
                        )

                        // Type badges
                        Row(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            pokemonData!!.types.forEach { type ->
                                TypeBadge(typeName = type)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Basic Info Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Weight
                    InfoCard(
                        title = "${(pokemonData!!.weight * 0.1f)} kg",
                        subtitle = "Peso",
                        icon = "⚖️",
                        modifier = Modifier.weight(1f)
                    )

                    // Height
                    InfoCard(
                        title = "${(pokemonData!!.height * 0.1f)} m",
                        subtitle = "Altura",
                        icon = "📏",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Description
                if (pokemonData!!.description != null) {
                    Text(
                        text = pokemonData!!.description!!,
                        fontSize = 14.sp,
                        color = Color.Black,
                        lineHeight = 20.sp,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }

                // Stats Section
                Text(
                    text = "Estadísticas",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Stats List
                pokemonData!!.stats.forEach { (statName, value) ->
                    StatRow(
                        statName = statName,
                        value = value,
                        maxValue = 150,
                        color = getPokemonTypeColor(pokemonData!!.types.firstOrNull() ?: "normal")
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun TypeBadge(typeName: String) {
    Box(
        modifier = Modifier
            .background(
                Color.White.copy(alpha = 0.9f),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = typeName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    subtitle: String,
    icon: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = icon,
                fontSize = 20.sp,
                modifier = Modifier.padding(end = 8.dp)
            )

            Column {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
private fun StatRow(
    statName: String,
    value: Int,
    maxValue: Int,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = statName,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = Modifier.width(100.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(20.dp)
                .padding(horizontal = 8.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (value.toFloat() / maxValue).coerceAtMost(1f))
                    .background(color, RoundedCornerShape(10.dp))
            )
        }

        Text(
            text = value.toString().padStart(3, '0'),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.width(40.dp),
            textAlign = TextAlign.End
        )
    }
}

private fun getPokemonTypeColor(type: String): Color {
    return when (type.lowercase()) {
        "fire", "fuego" -> Color(0xFFFF6B6B)
        "water", "agua" -> Color(0xFF4ECDC4)
        "grass", "planta" -> Color(0xFF95E1A3)
        "electric", "eléctrico" -> Color(0xFFFFE66D)
        "psychic", "psíquico" -> Color(0xFFFF8A80)
        "ice", "hielo" -> Color(0xFF81D4FA)
        "dragon", "dragón" -> Color(0xFF9C27B0)
        "dark", "siniestro" -> Color(0xFF424242)
        "fairy", "hada" -> Color(0xFFE1BEE7)
        "poison", "veneno" -> Color(0xFFBA68C8)
        "bug", "bicho" -> Color(0xFFA5D6A7)
        "ghost", "fantasma" -> Color(0xFF9E9E9E)
        "steel", "acero" -> Color(0xFFB0BEC5)
        "flying", "volador" -> Color(0xFF90CAF9)
        "normal" -> Color(0xFFE0E0E0)
        "fighting", "lucha" -> Color(0xFFFF8A65)
        "rock", "roca" -> Color(0xFFBCAAA4)
        "ground", "tierra" -> Color(0xFFD7CCC8)
        else -> Color(0xFFE0E0E0)
    }
}

private fun getStatColor(statName: String): Color {
    return when (statName.lowercase()) {
        "hp" -> Color(0xFF66BB6A)
        "ataque", "attack" -> Color(0xFF66BB6A)
        "defensa", "defense" -> Color(0xFF66BB6A)
        "ataque especial", "special-attack" -> Color(0xFF66BB6A)
        "defensa especial", "special-defense" -> Color(0xFF66BB6A)
        "velocidad", "speed" -> Color(0xFF66BB6A)
        else -> Color(0xFF66BB6A)
    }
}