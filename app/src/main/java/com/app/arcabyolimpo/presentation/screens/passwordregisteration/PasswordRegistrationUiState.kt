package com.app.arcabyolimpo.presentation.screens.passwordregisteration

import com.app.arcabyolimpo.domain.model.password.UpdatePassword

/**
 * UI state model representing the current state of the password registration screen.
 *
 * This state is used to manage and observe updates during the password creation or
 * reset process, including loading progress, success response, and potential errors.
 *
 * @property response The [UpdatePassword] response object returned after a successful password registration.
 * @property isLoading Indicates whether the password registration request is currently in progress.
 * @property error An optional error message displayed when the registration process fails.
 */

data class PasswordRegistrationUiState(
    val response: UpdatePassword? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
