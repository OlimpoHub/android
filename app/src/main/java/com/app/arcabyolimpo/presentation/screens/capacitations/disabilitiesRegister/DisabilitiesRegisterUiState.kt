package com.app.arcabyolimpo.presentation.screens.capacitations.disabilitiesRegister

data class DisabilitiesRegisterUiState(
    val nombre: String = "",
    val caracteristicas: String = "",
    val isNombreError: Boolean = false,
    val isCaracteristicasError: Boolean = false,
    val error: String? = null,
)
