package com.app.arcabyolimpo.presentation.screens.passwordregisteration

import com.app.arcabyolimpo.domain.model.password.UpdatePassword

data class PasswordRegistrationUiState(
    val response: UpdatePassword? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
