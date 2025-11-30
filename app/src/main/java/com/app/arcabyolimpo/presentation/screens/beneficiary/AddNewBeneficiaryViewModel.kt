package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryFormData
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.usecase.beneficiaries.PostAddNewBeneficiary
import com.app.arcabyolimpo.domain.usecase.disabilities.GetDisabilitiesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * ViewModel responsible for managing the UI state of the register beneficiary screen.
 *
 * This class interacts with the [PostAddNewBeneficiary] to post all the data in the domain layer
 * and exposes a [StateFlow] of [AddNewBeneficiaryUiState] that the UI observes to render updates.
 *
 * @property postAddNewBeneficiary Use case for posting the new beneficiary.
 * @property getDisabilitiesUseCase Use case for retrieving the list of disabilities.
 */
@HiltViewModel
class AddNewBeneficiaryViewModel @Inject constructor(
    private val postAddNewBeneficiary: PostAddNewBeneficiary,
    private val getDisabilitiesUseCase: GetDisabilitiesUseCase
) : ViewModel() {

    /** Backing property for the beneficiary UI state. */
    private val _uiState = MutableStateFlow(AddNewBeneficiaryUiState())
    val uiState: StateFlow<AddNewBeneficiaryUiState> = _uiState.asStateFlow()

    private val _formData = MutableStateFlow(BeneficiaryFormData())
    val formData: StateFlow<BeneficiaryFormData> = _formData.asStateFlow()

    // CAMBIO: Ahora es Map<String, String?> para almacenar mensajes de error
    private val _fieldErrors = MutableStateFlow<Map<String, String?>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, String?>> = _fieldErrors.asStateFlow()

    private val _disabilities = MutableStateFlow<List<Disability>>(emptyList())
    val disabilities: StateFlow<List<Disability>> = _disabilities.asStateFlow()


    /**
     * Loads the list of disabilities from the repository.
     */
    fun loadDisabilities() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getDisabilitiesUseCase().collect { result ->
                when (result) {
                    is Result.Loading -> {
                        _uiState.update { it.copy(isLoading = true, error = null) }
                    }

                    is Result.Success -> {
                        _disabilities.value = result.data
                        _uiState.update { it.copy(isLoading = false) }
                    }

                    is Result.Error -> {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Error al cargar discapacidades: ${result.exception.message}"
                            )
                        }
                    }
                }
            }
        }
    }

    /**
     * Adds a new beneficiary by validating the form and sending data to the repository.
     */
    fun addNewBeneficiary() {
        if (!validateForm()) return

        viewModelScope.launch {
            val beneficiaryDto = BeneficiaryDto(
                id = UUID.randomUUID().toString(),
                firstName = _formData.value.nombre,
                paternalName = _formData.value.apellidoPaterno,
                maternalName = _formData.value.apellidoMaterno,
                birthdate = _formData.value.fechaNacimiento,
                emergencyNumber = _formData.value.numeroEmergencia,
                emergencyName = _formData.value.nombreContactoEmergencia,
                emergencyRelation = _formData.value.relacionContactoEmergencia,
                details = _formData.value.descripcion,
                entryDate = _formData.value.fechaIngreso,
                image = _formData.value.foto,
                disabilities = _formData.value.disabilities,
                status = 1
            )

            postAddNewBeneficiary(beneficiaryDto).collect { result ->
                _uiState.update { state ->
                    when (result) {
                        is Result.Loading -> state.copy(
                            isLoading = true,
                            error = null,
                            isSuccess = false
                        )

                        is Result.Success -> state.copy(
                            addNewBeneficiary = result.data,
                            isLoading = false,
                            error = null,
                            isSuccess = true
                        )

                        is Result.Error -> state.copy(
                            isLoading = false,
                            error = result.exception.message,
                            isSuccess = false
                        )
                    }
                }
            }
        }
    }

    /**
     * Updates the form data with a lambda function.
     */
    fun updateFormData(update: BeneficiaryFormData.() -> BeneficiaryFormData) {
        _formData.update { it.update() }
        clearFieldErrors()
    }

    /**
     * Resets the form to its initial state.
     */
    fun resetForm() {
        _formData.value = BeneficiaryFormData()
        _fieldErrors.value = emptyMap()
        _uiState.update { it.copy(isSuccess = false, error = null) }
    }

    /**
     * Clears all field errors.
     */
    private fun clearFieldErrors() {
        _fieldErrors.value = emptyMap()
    }

    /**
     * Validates the form data and returns true if all fields are valid.
     */
    private fun validateForm(): Boolean {
        val data = _formData.value
        val errors = mutableMapOf<String, String?>()
        val dateRegex = Regex("^\\d{2}/\\d{2}/\\d{4}$")

        // Validar nombre
        if (data.nombre.isBlank()) {
            errors["nombre"] = "El nombre es obligatorio"
        }

        // Validar apellido paterno
        if (data.apellidoPaterno.isBlank()) {
            errors["apellidoPaterno"] = "El apellido paterno es obligatorio"
        }

        // Validar apellido materno
        if (data.apellidoMaterno.isBlank()) {
            errors["apellidoMaterno"] = "El apellido materno es obligatorio"
        }

        // Validar fecha de nacimiento
        if (data.fechaNacimiento.isBlank()) {
            errors["fechaNacimiento"] = "La fecha de nacimiento es obligatoria"
        } else if (!dateRegex.matches(data.fechaNacimiento)) {
            errors["fechaNacimiento"] = "Formato inválido. Use DD/MM/YYYY"
        }

        // Validar número de emergencia
        if (data.numeroEmergencia.isBlank()) {
            errors["numeroEmergencia"] = "El número de teléfono es obligatorio"
        } else if (data.numeroEmergencia.length != 10) {
            errors["numeroEmergencia"] = "El número debe tener 10 dígitos"
        } else if (!data.numeroEmergencia.all { it.isDigit() }) {
            errors["numeroEmergencia"] = "El número solo debe contener dígitos"
        }

        // Validar nombre de contacto de emergencia
        if (data.nombreContactoEmergencia.isBlank()) {
            errors["nombreContactoEmergencia"] = "El nombre del contacto es obligatorio"
        }

        // Validar relación del contacto de emergencia
        if (data.relacionContactoEmergencia.isBlank()) {
            errors["relacionContactoEmergencia"] = "La relación del tutor es obligatoria"
        }

        // Validar fecha de ingreso
        if (data.fechaIngreso.isBlank()) {
            errors["fechaIngreso"] = "La fecha de ingreso es obligatoria"
        } else if (!dateRegex.matches(data.fechaIngreso)) {
            errors["fechaIngreso"] = "Formato inválido. Use DD/MM/YYYY"
        }

        // Validar discapacidad
        if (data.disabilities.isEmpty()) {
            errors["discapacidad"] = "Debe seleccionar al menos una discapacidad"
        }

        // Validar descripción (opcional, depende de tus requisitos)
        if (data.descripcion.isBlank()) {
            errors["descripcion"] = "La descripción es obligatoria"
        }

        _fieldErrors.value = errors
        return errors.isEmpty()
    }
}