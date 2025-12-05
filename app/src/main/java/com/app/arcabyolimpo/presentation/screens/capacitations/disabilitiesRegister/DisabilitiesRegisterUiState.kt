package com.app.arcabyolimpo.presentation.screens.capacitations.disabilitiesRegister

/**
 * Represents the UI state for the Disability Registration screen.
 *
 * This data class holds all the information related to user input and validation
 * for the new disability form.
 *
 * @param nombre The name of the disability being registered.
 * @param caracteristicas The characteristics or description of the disability.
 * @param isNombreError A boolean flag indicating if the 'nombre' field has a validation error.
 * @param isCaracteristicasError A boolean flag indicating if the 'caracteristicas' field has a validation error.
 * @param error A nullable string for displaying a general error message (e.g., if registration fails).
 */
data class DisabilitiesRegisterUiState(
    val nombre: String = "",
    val caracteristicas: String = "",
    val isNombreError: Boolean = false,
    val isCaracteristicasError: Boolean = false,
    val error: String? = null,
)
