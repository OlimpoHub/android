package com.app.arcabyolimpo.presentation.screens.tokenverification

import com.app.arcabyolimpo.domain.model.password.VerifyToken

/**
 * UI state data class for the token verification screen.
 *
 * This class represents the current state of the token verification process,
 * including the API response, loading status, and potential error message.
 *
 * @param response The result of the token verification request, if successful.
 * @param isLoading Indicates whether the token verification process is currently ongoing.
 * @param error Contains an error message if the verification fails, otherwise null.
 */

data class TokenVerificationUiState(
    val response: VerifyToken? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
