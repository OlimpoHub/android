package com.app.arcabyolimpo.presentation.screens.ExternalCollab.RegisterExternalCollab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab
import com.app.arcabyolimpo.domain.usecase.ExternalCollaborator.GetAllCollabsUseCase
import com.app.arcabyolimpo.domain.usecase.ExternalCollaborator.RegisterExternalCollab.RegisterCollabUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExternalCollabRegisterViewModel @Inject constructor(
    private val registerCollabUseCase: RegisterCollabUseCase,
    private val getAllCollabsUseCase: GetAllCollabsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExternalCollabRegisterUiState())
    val uiState: StateFlow<ExternalCollabRegisterUiState> = _uiState.asStateFlow()

    fun updateIsActive(value: Boolean) {
        _uiState.value = _uiState.value.copy(isActive = value)
    }

    fun updateFirstName(value: String) {
        _uiState.value = _uiState.value.copy(
            firstName = value,
            firstNameError = if (value.isBlank()) "El nombre es requerido" else null
        )
    }

    fun updateLastName(value: String) {
        _uiState.value = _uiState.value.copy(
            lastName = value,
            lastNameError = if (value.isBlank()) "El apellido es requerido" else null
        )
    }

    fun updateSecondLastName(value: String) {
        _uiState.value = _uiState.value.copy(secondLastName = value)
    }

    fun updateEmail(value: String) {
        _uiState.value = _uiState.value.copy(
            email = value,
            emailError = if (!isValidEmail(value)) "Email inválido" else null
        )
    }

    fun updatePhone(value: String) {
        _uiState.value = _uiState.value.copy(
            phone = value,
            phoneError = if (value.length < 10) "Teléfono inválido" else null
        )
    }

    fun updateBirthDate(value: String) {
        _uiState.value = _uiState.value.copy(birthDate = value)
    }

    fun updateDegree(value: String) {
        _uiState.value = _uiState.value.copy(degree = value)
    }

    fun registerCollab() {
        if (!validateForm()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            // Check for duplicates first
            getAllCollabsUseCase().fold(
                onSuccess = { existingCollabs ->
                    val duplicate = existingCollabs.find { existing ->
                        existing.email.equals(_uiState.value.email, ignoreCase = true) ||
                                (existing.firstName.equals(_uiState.value.firstName, ignoreCase = true) &&
                                        existing.lastName.equals(_uiState.value.lastName, ignoreCase = true) &&
                                        existing.secondLastName.equals(_uiState.value.secondLastName, ignoreCase = true))
                    }

                    if (duplicate != null) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Este colaborador externo ya está registrado"
                        )
                        return@launch
                    }

                    // If no duplicate, proceed with registration
                    proceedWithRegistration()
                },
                onFailure = {
                    // If we can't check, proceed anyway
                    proceedWithRegistration()
                }
            )
        }
    }

    private suspend fun proceedWithRegistration() {
        val collab = ExternalCollab(
            id = null,
            roleId = _uiState.value.roleId,
            firstName = _uiState.value.firstName,
            lastName = _uiState.value.lastName,
            secondLastName = _uiState.value.secondLastName,
            birthDate = _uiState.value.birthDate,
            degree = _uiState.value.degree,
            email = _uiState.value.email,
            phone = _uiState.value.phone,
            isActive = _uiState.value.isActive,
            photoUrl = null
        )

        android.util.Log.d("RegisterCollab", "Starting registration...")

        registerCollabUseCase(collab).fold(
            onSuccess = {
                android.util.Log.d("RegisterCollab", "SUCCESS!")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    success = true,
                    successMessage = "Colaborador externo registrado exitosamente",
                    error = null
                )
            },
            onFailure = { exception ->
                android.util.Log.e("RegisterCollab", "FAILURE: ${exception.message}", exception)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = exception.message ?: "Error al registrar"
                )
            }
        )
    }

    fun resetState() {
        _uiState.value = ExternalCollabRegisterUiState()
    }

    private fun validateForm(): Boolean {
        val state = _uiState.value

        val firstNameError = if (state.firstName.isBlank()) "El nombre es requerido" else null
        val lastNameError = if (state.lastName.isBlank()) "El apellido es requerido" else null
        val emailError = if (!isValidEmail(state.email)) "Email inválido" else null
        val phoneError = if (state.phone.length < 10) "Teléfono inválido" else null

        _uiState.value = _uiState.value.copy(
            firstNameError = firstNameError,
            lastNameError = lastNameError,
            emailError = emailError,
            phoneError = phoneError
        )

        return firstNameError == null &&
                lastNameError == null &&
                emailError == null &&
                phoneError == null
    }

    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }




}