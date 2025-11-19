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

    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

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
                disabilities = _formData.value.discapacidad,
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
        val errors = mutableMapOf<String, Boolean>()
        val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        val phoneRegex = Regex("^\\d{10}$")

        if (data.nombre.isBlank()) errors["nombre"] = true
        if (data.apellidoPaterno.isBlank()) errors["apellidoPaterno"] = true
        if (data.apellidoMaterno.isBlank()) errors["apellidoMaterno"] = true
        if (data.fechaNacimiento.isBlank()) errors["fechaNacimiento"] = true
        if (data.numeroEmergencia.isBlank()) errors["numeroEmergencia"] = true
        if (data.nombreContactoEmergencia.isBlank()) errors["nombreContactoEmergencia"] = true
        if (data.relacionContactoEmergencia.isBlank()) errors["relacionContactoEmergencia"] = true
        if (data.fechaIngreso.isBlank()) errors["fechaIngreso"] = true
        if (data.discapacidad.isBlank()) errors["discapacidad"] = true

        if (data.fechaNacimiento.isNotBlank() && !dateRegex.matches(data.fechaNacimiento)) {
            errors["fechaNacimiento"] = true
        }
        if (data.fechaIngreso.isNotBlank() && !dateRegex.matches(data.fechaIngreso)) {
            errors["fechaIngreso"] = true
        }
        if (data.numeroEmergencia.isNotBlank() && !phoneRegex.matches(data.numeroEmergencia)) {
            errors["numeroEmergencia"] = true
        }

        _fieldErrors.value = errors
        return errors.isEmpty()
    }
}