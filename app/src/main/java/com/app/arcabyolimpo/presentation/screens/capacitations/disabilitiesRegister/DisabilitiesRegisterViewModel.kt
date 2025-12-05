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

/**
 * ViewModel for the DisabilitiesRegisterScreen.
 *
 * This class is responsible for managing the UI state and business logic related to
 * registering a new disability. It handles user input from the form, validates the data,
 * and communicates with the corresponding use case to save the new disability.
 *
 * @param registerDisabilityUseCase The use case responsible for creating and persisting
 * a new disability entry.
 */
@HiltViewModel
class DisabilitiesRegisterViewModel
    @Inject
    constructor(
        private val registerDisabilityUseCase: RegisterDisabilityUseCase,
    ) : ViewModel() {
        var uiState by mutableStateOf(DisabilitiesRegisterUiState())
            private set

        /**
         * Updates the UI state when a form field's value changes.
         *
         * This function is called whenever the user types in one of the input fields. It updates
         * the appropriate property in the uiState and resets the corresponding error flag
         * to provide immediate feedback.
         *
         * @param field A string identifier for the field being changed (e.g., "nombre").
         * @param value The new text value entered by the user.
         */
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

        /**
         * Validates the form fields and proceeds with registration if the data is valid.
         *
         * It checks if the required fields (`nombre` and `caracteristicas`) are blank. If any
         * field is invalid, it updates the UI state to show error indicators. If all fields are
         * valid, it calls the private `register` function.
         *
         * @param onSuccess A callback function to be executed upon successful registration,
         *                  typically used for navigation.
         */
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

        /**
         * Calls the use case to save the new disability to the repository.
         *
         * This private function is executed only after successful validation. It constructs a
         * Disability domain model from the current UI state and passes it to the
         * registerDisabilityUseCase. If the operation is successful, it invokes the onSuccess
         * callback. If an error occurs, it updates the UI state with an error message.
         *
         * @param onSuccess The callback function to execute after the disability is successfully saved.
         */
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
