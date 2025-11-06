package com.app.arcabyolimpo.presentation.screens.tokenverification

import com.app.arcabyolimpo.domain.model.password.VerifyToken

data class TokenVerificationUiState(
    val response: VerifyToken? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)