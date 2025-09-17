package com.cuscatlan.pokemon.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import coil.compose.AsyncImage
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.data.model.Trainer
import java.time.format.DateTimeFormatter

@Composable
fun TrainerProfileScreen(
    trainer: Trainer,
    onEditProfile: () -> Unit,
    onEditPokemon: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
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

                Text(
                    text = "Perfil del entrenador",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            IconButton(onClick = onEditProfile) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = Color(0xFFFFB000)
                )
            }
        }

        // Trainer Info Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Photo
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(3.dp, Color(0xFFFFB000), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (trainer.photoUri != null) {
                        AsyncImage(
                            model = trainer.photoUri,
                            contentDescription = "Trainer photo",
                            modifier = Modifier.fillMaxSize().clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "No photo",
                            modifier = Modifier.size(80.dp),
                            tint = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                Text(
                    text = trainer.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Details Grid
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Hobby
                    trainer.hobby?.let { hobby ->
                        ProfileDetailCard(
                            label = "Pasatiempo",
                            value = hobby
                        )
                    }

                    // Age
                    ProfileDetailCard(
                        label = "Edad",
                        value = "${trainer.age} años"
                    )

                    // Identification (only if not empty or null)
                    trainer.identificationNumber?.takeIf { it.isNotEmpty() }?.let { id ->
                        ProfileDetailCard(
                            label = if (trainer.isAdult) "DUI" else "Carnet de minoridad",
                            value = id
                        )
                    }
                }
            }
        }

        // Pokemon Team Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mostrar equipo Pokémon",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            TextButton(onClick = onEditPokemon) {
                Text("Editar equipo", color = Color(0xFFFFB000))
            }
        }

        if (trainer.selectedPokemon.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay Pokémon seleccionados aún",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            trainer.selectedPokemon.forEach { pokemon ->
                PokemonTeamCard(pokemon = pokemon)
            }
        }

        // Bottom Spacer
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileDetailCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}

@Composable
private fun PokemonTeamCard(
    pokemon: PokemonDisplayData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Pokemon Image (Sprite)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            getPokemonTypeColor(pokemon.types.firstOrNull() ?: "normal").copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.name,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Pokemon Info
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    // Name
                    Text(
                        text = pokemon.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Types
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(pokemon.types) { type ->
                            TypeBadge(typeName = type)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats with progress bars
            Text(
                text = "Barra de progreso de stats",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            pokemon.stats.forEach { (statName, value) ->
                StatProgressBar(
                    statName = statName,
                    value = value,
                    maxValue = 150,
                    color = getPokemonTypeColor(pokemon.types.firstOrNull() ?: "normal")
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun TypeBadge(typeName: String) {
    Box(
        modifier = Modifier
            .background(
                getPokemonTypeColor(typeName),
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = typeName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White
        )
    }
}

@Composable
private fun StatProgressBar(
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
            modifier = Modifier.width(120.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .background(Color.LightGray.copy(alpha = 0.3f), RoundedCornerShape(4.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(fraction = (value.toFloat() / maxValue).coerceAtMost(1f))
                    .background(color, RoundedCornerShape(4.dp))
            )
        }

        Text(
            text = value.toString(),
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

private fun getStatColor(value: Int): Color {
    return when {
        value < 50 -> Color(0xFFFF5722) // Red for low stats
        value < 100 -> Color(0xFFFF9800) // Orange for medium stats
        else -> Color(0xFF4CAF50) // Green for high stats
    }
}