package com.cuscatlan.pokemon.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cuscatlan.pokemon.data.model.PokemonDisplayData
import com.cuscatlan.pokemon.data.model.Trainer
import com.cuscatlan.pokemon.data.model.TrainerFormData
import com.cuscatlan.pokemon.presentation.utils.DUIValidation
import com.cuscatlan.pokemon.presentation.utils.CarnetValidation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class TrainerFormViewModel : ViewModel() {
    
    private val _formState = MutableStateFlow(TrainerFormData())
    val formState: StateFlow<TrainerFormData> = _formState.asStateFlow()
    
    private val _validationErrors = MutableStateFlow<Map<String, String>>(emptyMap())
    val validationErrors: StateFlow<Map<String, String>> = _validationErrors.asStateFlow()
    
    private val _trainer = MutableStateFlow<Trainer?>(null)
    val trainer: StateFlow<Trainer?> = _trainer.asStateFlow()
    
    private val _isFormValid = MutableStateFlow(false)
    val isFormValid: StateFlow<Boolean> = _isFormValid.asStateFlow()
    
    fun updateName(name: String) {
        _formState.value = _formState.value.copy(name = name)
        validateName(name)
        updateFormValidity()
    }

    fun updatePhotoUri(uri: String?) {
        _formState.value = _formState.value.copy(photoUri = uri)
        validatePhoto(uri)
        updateFormValidity()
    }

    fun updateHobby(hobby: String) {
        _formState.value = _formState.value.copy(hobby = hobby)
        updateFormValidity()
    }

    fun updateBirthDate(date: LocalDate?) {
        _formState.value = _formState.value.copy(birthDate = date)
        validateBirthDate(date)

        // Revalidate DUI when birth date changes (adult status might change)
        val currentDui = _formState.value.identificationNumber
        if (currentDui.isNotEmpty()) {
            validateIdentification(currentDui)
        }
        updateFormValidity()
    }

    fun updateIdentificationNumber(number: String) {
        _formState.value = _formState.value.copy(identificationNumber = number)
        validateIdentification(number)
        updateFormValidity()
    }
    
    fun updateSelectedPokemon(pokemon: List<PokemonDisplayData>) {
        _formState.value = _formState.value.copy(selectedPokemon = pokemon)
        updateFormValidity()
    }
    
    private fun validateName(name: String) {
        val currentErrors = _validationErrors.value.toMutableMap()
        if (name.trim().isEmpty()) {
            currentErrors["name"] = "El nombre es requerido"
        } else if (name.trim().length < 2) {
            currentErrors["name"] = "El nombre debe tener al menos 2 caracteres"
        } else {
            currentErrors.remove("name")
        }
        _validationErrors.value = currentErrors
    }

    private fun validatePhoto(uri: String?) {
        val currentErrors = _validationErrors.value.toMutableMap()
        if (uri == null) {
            currentErrors["photo"] = "La foto es requerida"
        } else {
            currentErrors.remove("photo")
        }
        _validationErrors.value = currentErrors
    }

    private fun validateBirthDate(date: LocalDate?) {
        val currentErrors = _validationErrors.value.toMutableMap()
        if (date == null) {
            currentErrors["birthDate"] = "La fecha de nacimiento es requerida"
        } else if (date.isAfter(LocalDate.now())) {
            currentErrors["birthDate"] = "La fecha de nacimiento no puede ser futura"
        } else {
            currentErrors.remove("birthDate")
        }
        _validationErrors.value = currentErrors
    }

    private fun validateIdentification(identification: String) {
        val birthDate = _formState.value.birthDate
        val currentErrors = _validationErrors.value.toMutableMap()

        if (birthDate != null && isAdult(birthDate)) {
            // Adult - DUI is required and must be valid
            if (identification.trim().isEmpty()) {
                currentErrors["identificationNumber"] = "El DUI es requerido para mayores de edad"
            } else if (!DUIValidation.isValidDUI(identification)) {
                currentErrors["identificationNumber"] = "Formato de DUI inválido"
            } else {
                currentErrors.remove("identificationNumber")
            }
        } else {
            // Minor - carnet is optional but if provided, must be valid
            if (identification.trim().isNotEmpty() && !CarnetValidation.isValidCarnet(identification)) {
                currentErrors["identificationNumber"] = "Formato de carnet inválido"
            } else {
                currentErrors.remove("identificationNumber")
            }
        }
        _validationErrors.value = currentErrors
    }

    private fun isAdult(birthDate: LocalDate): Boolean {
        return LocalDate.now().minusYears(18).isAfter(birthDate) ||
                LocalDate.now().minusYears(18).isEqual(birthDate)
    }

    private fun updateFormValidity() {
        viewModelScope.launch {
            val formData = _formState.value
            val hasName = formData.name.trim().isNotEmpty() && formData.name.trim().length >= 2
            val hasPhoto = formData.photoUri != null
            val hasBirthDate = formData.birthDate != null && !formData.birthDate!!.isAfter(LocalDate.now())

            val hasValidIdentification = if (formData.birthDate != null && isAdult(formData.birthDate!!)) {
                // Adult - DUI required and valid
                formData.identificationNumber.trim().isNotEmpty() && DUIValidation.isValidDUI(formData.identificationNumber)
            } else {
                // Minor - carnet optional, but if provided must be valid
                formData.identificationNumber.trim().isEmpty() || CarnetValidation.isValidCarnet(formData.identificationNumber)
            }

            _isFormValid.value = hasName && hasPhoto && hasBirthDate && hasValidIdentification && _validationErrors.value.isEmpty()
        }
    }
    
    fun saveTrainer() {
        viewModelScope.launch {
            val formData = _formState.value
            if (_isFormValid.value) {
                _trainer.value = formData.toTrainer()
            }
        }
    }
    
    fun loadTrainer(trainer: Trainer) {
        _trainer.value = trainer
        _formState.value = TrainerFormData(
            name = trainer.name,
            photoUri = trainer.photoUri,
            hobby = trainer.hobby ?: "",
            birthDate = trainer.birthDate,
            identificationNumber = trainer.identificationNumber ?: "",
            selectedPokemon = trainer.selectedPokemon
        )
        updateFormValidity()
    }
    
    fun clearForm() {
        _formState.value = TrainerFormData()
        _validationErrors.value = emptyMap()
        _trainer.value = null
        _isFormValid.value = false
    }
}