package com.app.arcabyolimpo.presentation.screens.capacitations.disabilitiesRegister

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.model.disabilities.Disability
import com.app.arcabyolimpo.domain.usecase.disabilities.RegisterDisabilityUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.http.Field
import javax.inject.Inject

@HiltViewModel
class DisabilitiesRegisterViewModel
    @Inject
    constructor(
        private val registerDisabilityUseCase: RegisterDisabilityUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(DisabilitiesRegisterUiState())
            private set

        fun onFieldChange(
            field: String,
            value: String,
        ) {
            when (field) {
                "nombre" ->
                    uiState =
                        uiState.copy(
                            nombre = value,
                            isNombreError = false,
                        )
                "caracteristicas" ->
                    uiState =
                        uiState.copy(
                            caracteristicas = value,
                            isCaracteristicasError = false,
                        )
            }
        }

        fun validateAndRegister(onSuccess: () -> Unit) {
            val nombreInvalid = uiState.nombre.isBlank()
            val caracteristicasInvalid = uiState.caracteristicas.isBlank()

            uiState =
                uiState.copy(
                    isNombreError = nombreInvalid,
                    isCaracteristicasError = caracteristicasInvalid,
                )

            val hasError =
                listOf(
                    nombreInvalid,
                    caracteristicasInvalid,
                ).any { it }

            if (!hasError) {
                register(onSuccess)
            }
        }

        private fun register(onSuccess: () -> Unit) {
            viewModelScope.launch {
                try {
                    val newDisability =
                        Disability(
                            id = "",
                            name = uiState.nombre,
                            characteristics = uiState.caracteristicas,
                        )
                    registerDisabilityUseCase(newDisability)
                    onSuccess()
                } catch (e: Exception) {
                    uiState = uiState.copy(error = "Error al registrar discapacidad: ${e.message}")
                }
            }
        }
    }
