package com.cuscatlan.pokemon.presentation.utils

object DUIValidation {

    fun formatDUI(input: String): String {
        val digitsOnly = input.filter { it.isDigit() }
        return when {
            digitsOnly.length <= 8 -> digitsOnly
            digitsOnly.length == 9 -> "${digitsOnly.substring(0, 8)}-${digitsOnly.substring(8)}"
            else -> "${digitsOnly.substring(0, 8)}-${digitsOnly.substring(8, 9)}"
        }
    }

    fun isValidDUI(dui: String): Boolean {
        val formatted = formatDUI(dui)
        if (!formatted.matches(Regex("^\\d{8}-\\d$"))) return false

        val digits = formatted.replace("-", "")
        if (digits.length != 9) return false

        // Validación del algoritmo de DUI de El Salvador
        val weights = intArrayOf(2, 3, 4, 5, 6, 7, 8, 9)
        var sum = 0

        for (i in 0..7) {
            sum += digits[i].toString().toInt() * weights[i]
        }

        val remainder = sum % 11
        val checkDigit = when {
            remainder < 2 -> 0
            else -> 11 - remainder
        }

        return checkDigit == digits[8].toString().toInt()
    }

    fun getFormattedDUIWithCursor(input: String, cursorPosition: Int): Pair<String, Int> {
        val digitsOnly = input.filter { it.isDigit() }
        val formatted = formatDUI(digitsOnly)

        // Ajustar posición del cursor después del formato
        val newCursorPosition = when {
            cursorPosition <= 8 -> cursorPosition
            cursorPosition == 9 && formatted.contains("-") -> 10
            else -> formatted.length
        }

        return Pair(formatted, newCursorPosition)
    }
}

object CarnetValidation {
    fun isValidCarnet(carnet: String): Boolean {
        // Validación básica para carnet de minoridad
        // Puede ser alfanumérico y tener entre 6 y 15 caracteres
        return carnet.trim().length in 6..15 && carnet.trim().matches(Regex("^[A-Za-z0-9]+$"))
    }
}