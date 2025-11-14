package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryDto
import com.app.arcabyolimpo.data.remote.dto.beneficiaries.BeneficiaryFormData
import com.app.arcabyolimpo.domain.usecase.beneficiaries.PostAddNewBeneficiary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import com.app.arcabyolimpo.domain.common.Result
import javax.inject.Inject


@HiltViewModel
class AddNewBeneficiaryViewModel @Inject constructor(
    private val postAddNewBeneficiary: PostAddNewBeneficiary
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddNewBeneficiaryUiState())
    val uiState: StateFlow<AddNewBeneficiaryUiState> = _uiState.asStateFlow()
    private val _formData = MutableStateFlow(BeneficiaryFormData())
    val formData: StateFlow<BeneficiaryFormData> = _formData.asStateFlow()
    private val _fieldErrors = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val fieldErrors: StateFlow<Map<String, Boolean>> = _fieldErrors.asStateFlow()

    fun addNewBeneficiary() {
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
                            error = result.exception.message ?: "Error al crear el beneficiario",
                            isSuccess = false
                        )
                    }
                }
            }

        }
    }

    fun updateFormData(update: BeneficiaryFormData.() -> BeneficiaryFormData) {
        _formData.update { it.update() }
        clearFieldErrors()
    }

    fun resetForm() {
        _formData.value = BeneficiaryFormData()
        _fieldErrors.value = emptyMap()
        _uiState.update { it.copy(isSuccess = false, error = null) }
    }

    private fun clearFieldErrors() {
        _fieldErrors.value = emptyMap()
    }

    private fun validateForm(): Boolean {
        val data = _formData.value
        val errors = mutableMapOf<String, Boolean>()
        val dateRegex = Regex("^\\d{4}-\\d{2}-\\d{2}$")
        val phoneRegex = Regex("^\\d{10}$") // 10 dígitos para número telefónico

        if (data.nombre.isBlank()) errors["nombre"] = true
        if (data.apellidoPaterno.isBlank()) errors["apellidoPaterno"] = true
        if (data.apellidoMaterno.isBlank()) errors["apellidoMaterno"] = true
        if (data.fechaNacimiento.isBlank()) errors["fechaNacimiento"] = true
        if (data.numeroEmergencia.isBlank()) errors["numeroEmergencia"] = true
        if (data.nombreContactoEmergencia.isBlank()) errors["nombreContactoEmergencia"] = true
        if (data.relacionContactoEmergencia.isBlank()) errors["relacionContactoEmergencia"] = true
        if (data.descripcion.isBlank()) errors["descripcion"] = true
        if (data.fechaIngreso.isBlank()) errors["fechaIngreso"] = true
        if (data.foto.isBlank()) errors["foto"] = true
        if (data.discapacidad.isBlank()) errors["discapacidad"] = true

        // Validación de formato de fecha de nacimiento
        if (data.fechaNacimiento.isNotBlank() && !dateRegex.matches(data.fechaNacimiento)) {
            errors["fechaNacimiento"] = true
        }

        // Validación de formato de fecha de ingreso
        if (data.fechaIngreso.isNotBlank() && !dateRegex.matches(data.fechaIngreso)) {
            errors["fechaIngreso"] = true
        }

        // Validación de formato de número de emergencia
        if (data.numeroEmergencia.isNotBlank() && !phoneRegex.matches(data.numeroEmergencia)) {
            errors["numeroEmergencia"] = true
        }

        _fieldErrors.value = errors
        return errors.isEmpty()
    }

}
