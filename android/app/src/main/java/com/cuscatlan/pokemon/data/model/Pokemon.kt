package com.cuscatlan.pokemon.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonListItem>
)

@Serializable
data class PokemonListItem(
    val name: String,
    val url: String
) {
    val id: Int
        get() = url.split("/").dropLast(1).last().toInt()
}

@Serializable
data class Pokemon(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val sprites: Sprites,
    val stats: List<Stat>,
    val types: List<TypeElement>
)

@Serializable
data class Sprites(
    @SerialName("front_default")
    val frontDefault: String?,
    val other: Other?
)

@Serializable
data class Other(
    val home: Home?
)

@Serializable
data class Home(
    @SerialName("front_default")
    val frontDefault: String?
)

@Serializable
data class Stat(
    @SerialName("base_stat")
    val baseStat: Int,
    val effort: Int,
    val stat: StatInfo
)

@Serializable
data class StatInfo(
    val name: String,
    val url: String
)

@Serializable
data class TypeElement(
    val slot: Int,
    val type: TypeInfo
)

@Serializable
data class TypeInfo(
    val name: String,
    val url: String
)

@Serializable
data class PokemonSpecies(
    val id: Int,
    val name: String,
    @SerialName("flavor_text_entries")
    val flavorTextEntries: List<FlavorTextEntry>
)

@Serializable
data class FlavorTextEntry(
    @SerialName("flavor_text")
    val flavorText: String,
    val language: Language
)

@Serializable
data class Language(
    val name: String,
    val url: String
)

data class PokemonDisplayData(
    val id: Int,
    val name: String,
    val imageUrl: String?,
    val types: List<String>,
    val stats: Map<String, Int>,
    val height: Int,
    val weight: Int,
    val description: String? = null,
    val isSelected: Boolean = false
) {
    companion object {
        fun fromPokemon(pokemon: Pokemon, species: PokemonSpecies? = null): PokemonDisplayData {
            val description = species?.flavorTextEntries
                ?.find { it.language.name == "es" }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ")
                ?: species?.flavorTextEntries
                    ?.find { it.language.name == "en" }?.flavorText?.replace("\n", " ")?.replace("\u000c", " ")

            return PokemonDisplayData(
                id = pokemon.id,
                name = pokemon.name.replaceFirstChar { it.uppercase() },
                imageUrl = pokemon.sprites.other?.home?.frontDefault ?: pokemon.sprites.frontDefault,
                types = pokemon.types.map { it.type.name.replaceFirstChar { char -> char.uppercase() } },
                height = pokemon.height,
                weight = pokemon.weight,
                description = description,
                stats = mapOf(
                    "HP" to (pokemon.stats.find { it.stat.name == "hp" }?.baseStat ?: 0),
                    "Ataque" to (pokemon.stats.find { it.stat.name == "attack" }?.baseStat ?: 0),
                    "Defensa" to (pokemon.stats.find { it.stat.name == "defense" }?.baseStat ?: 0),
                    "Ataque especial" to (pokemon.stats.find { it.stat.name == "special-attack" }?.baseStat ?: 0),
                    "Defensa especial" to (pokemon.stats.find { it.stat.name == "special-defense" }?.baseStat ?: 0),
                    "Velocidad" to (pokemon.stats.find { it.stat.name == "speed" }?.baseStat ?: 0)
                )
            )
        }
    }
}