package com.app.arcabyolimpo.presentation.screens.login

import com.app.arcabyolimpo.domain.model.auth.User

data class LoginUiState(
    val username: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val user: User? = null,
    val error: String? = null,
)
