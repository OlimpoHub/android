package com.app.arcabyolimpo.presentation.screens.passwordregisteration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.password.PostPasswordRegistrationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel responsible for handling the logic of the password registration process.
 *
 * This ViewModel communicates with the [PostPasswordRegistrationUseCase] to send a new
 * password registration request and updates the UI state accordingly through a [StateFlow].
 *
 * It exposes a read-only [uiState] to the UI layer, which reflects the current state of
 * the registration process — including loading status, success response, and errors.
 *
 * @property postPasswordRegistrationUseCase Use case responsible for executing the password registration logic.
 */

@HiltViewModel
class PasswordRegistrationViewModel
    @Inject
    constructor(
        private val postPasswordRegistrationUseCase: PostPasswordRegistrationUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PasswordRegistrationUiState())
        val uiState: StateFlow<PasswordRegistrationUiState> = _uiState.asStateFlow()

        /**
         * Executes the password registration request using the provided email and password.
         *
         * This function:
         * 1. Calls the [PostPasswordRegistrationUseCase] inside a coroutine.
         * 2. Collects its emitted [Result] states (Loading, Success, Error).
         * 3. Updates [_uiState] accordingly, allowing the UI to react in real time.
         *
         * @param email The email of the user registering a new password.
         * @param password The password the user wants to register.
         */
        fun postPasswordRegistration(
            email: String,
            password: String,
        ) {
            viewModelScope.launch {
                postPasswordRegistrationUseCase(email, password).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success ->
                                state.copy(
                                    response = result.data,
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
