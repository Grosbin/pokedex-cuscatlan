package com.cuscatlan.pokemon.data.model

import java.time.LocalDate

data class Trainer(
    val name: String,
    val photoUri: String?,
    val hobby: String?,
    val birthDate: LocalDate,
    val identificationNumber: String?,
    val selectedPokemon: List<PokemonDisplayData> = emptyList()
) {
    val isAdult: Boolean
        get() = LocalDate.now().minusYears(18).isAfter(birthDate) || 
                LocalDate.now().minusYears(18).isEqual(birthDate)
    
    val age: Int
        get() = LocalDate.now().year - birthDate.year - 
                if (LocalDate.now().dayOfYear < birthDate.dayOfYear) 1 else 0
}

data class TrainerFormData(
    val name: String = "",
    val photoUri: String? = null,
    val hobby: String = "",
    val birthDate: LocalDate? = null,
    val identificationNumber: String = "",
    val selectedPokemon: List<PokemonDisplayData> = emptyList()
) {
    fun toTrainer(): Trainer? {
        return if (isValid()) {
            Trainer(
                name = name.trim(),
                photoUri = photoUri,
                hobby = hobby.trim().takeIf { it.isNotEmpty() },
                birthDate = birthDate!!,
                identificationNumber = identificationNumber.trim().takeIf { it.isNotEmpty() },
                selectedPokemon = selectedPokemon
            )
        } else null
    }
    
    private fun isValid(): Boolean {
        return name.trim().isNotEmpty() && 
               birthDate != null &&
               (identificationNumber.trim().isNotEmpty() || !isAdult())
    }
    
    private fun isAdult(): Boolean {
        return birthDate?.let { 
            LocalDate.now().minusYears(18).isAfter(it) || 
            LocalDate.now().minusYears(18).isEqual(it) 
        } ?: false
    }
}