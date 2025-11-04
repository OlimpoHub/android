package com.app.arcabyolimpo.presentation.screens.splash

import com.app.arcabyolimpo.domain.model.auth.UserRole

data class SplashUiState(
    val isLoading: Boolean = true,
    val role: UserRole? = null, // null => no active session or unknown
    val error: String? = null,
)
