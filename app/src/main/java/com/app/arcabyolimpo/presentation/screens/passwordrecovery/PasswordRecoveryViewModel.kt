package com.app.arcabyolimpo.presentation.screens.passwordrecovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.password.PostPasswordRecoveryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the password recovery logic.
 *
 * It communicates with the domain layer via the [PostPasswordRecoveryUseCase] to
 * send password recovery requests and updates the UI state accordingly.
 *
 * The ViewModel exposes a [StateFlow] of [PasswordRecoveryUiState] that the UI
 * observes to reflect loading, success, or error states.
 *
 * @property postPasswordRecoveryUseCase Use case that handles the password recovery request logic.
 */

@HiltViewModel
class PasswordRecoveryViewModel
    @Inject
    constructor(
        private val postPasswordRecoveryUseCase: PostPasswordRecoveryUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PasswordRecoveryUiState())
        val uiState: StateFlow<PasswordRecoveryUiState> = _uiState.asStateFlow()

        /**
         * Sends a password recovery request using the provided email.
         *
         * The flow emitted by [postPasswordRecoveryUseCase] is collected to update
         * the UI state based on the type of result:
         *
         * - **Loading:** shows progress indicator
         * - **Success:** updates message and stops loading
         * - **Error:** displays error message and stops loading
         *
         * @param email The email address to which the recovery instructions will be sent.
         */
        fun postPasswordRecovery(email: String) {
            viewModelScope.launch {
                postPasswordRecoveryUseCase(email).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    message = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            is Result.Error ->
                                state.copy(
                                    error = result.exception.message,
                                    isLoading = false,
                                )
                        }
                    }
                }
            }
        }
    }
