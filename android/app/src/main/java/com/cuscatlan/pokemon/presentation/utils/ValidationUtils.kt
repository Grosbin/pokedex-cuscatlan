package com.cuscatlan.pokemon.presentation.utils

import java.time.LocalDate

object ValidationUtils {
    
    fun validateName(name: String): ValidationResult {
        return when {
            name.isBlank() -> ValidationResult.Error("Name is required")
            name.length < 2 -> ValidationResult.Error("Name must be at least 2 characters")
            else -> ValidationResult.Success
        }
    }
    
    fun validateDUI(dui: String): ValidationResult {
        if (dui.isBlank()) return ValidationResult.Error("DUI is required for adults")
        
        val cleanDui = dui.replace("-", "")
        
        return when {
            cleanDui.length != 9 -> ValidationResult.Error("DUI must have 9 digits")
            !cleanDui.all { it.isDigit() } -> ValidationResult.Error("DUI must contain only digits")
            else -> ValidationResult.Success
        }
    }
    
    fun formatDUI(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }.take(9)
        return when {
            digitsOnly.length <= 8 -> digitsOnly
            else -> "${digitsOnly.substring(0, 8)}-${digitsOnly.substring(8)}"
        }
    }
    
    fun validateBirthDate(date: LocalDate?): ValidationResult {
        return when {
            date == null -> ValidationResult.Error("Birth date is required")
            date.isAfter(LocalDate.now()) -> ValidationResult.Error("Birth date cannot be in the future")
            date.isBefore(LocalDate.now().minusYears(120)) -> ValidationResult.Error("Please enter a valid birth date")
            else -> ValidationResult.Success
        }
    }
    
    fun isAdult(birthDate: LocalDate): Boolean {
        return LocalDate.now().minusYears(18).isAfter(birthDate) || 
               LocalDate.now().minusYears(18).isEqual(birthDate)
    }
}

sealed class ValidationResult {
    object Success : ValidationResult()
    data class Error(val message: String) : ValidationResult()
    
    val isValid: Boolean
        get() = this is Success
        
    val errorMessage: String?
        get() = (this as? Error)?.message
}