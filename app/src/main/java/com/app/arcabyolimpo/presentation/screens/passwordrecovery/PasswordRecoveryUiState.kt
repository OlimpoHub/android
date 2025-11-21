package com.app.arcabyolimpo.presentation.screens.passwordrecovery

/**
 * UI state model representing the current state of the password recovery screen.
 *
 * This state is observed by the UI to reflect updates from the domain layer,
 * such as loading progress, success messages, or errors during the password
 * recovery process.
 *
 * @property message Optional message returned after a successful recovery request (e.g., email sent confirmation).
 * @property isLoading Indicates whether the recovery request is currently being processed.
 * @property error Optional error message displayed when the recovery request fails.
 */

data class PasswordRecoveryUiState(
    val message: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
