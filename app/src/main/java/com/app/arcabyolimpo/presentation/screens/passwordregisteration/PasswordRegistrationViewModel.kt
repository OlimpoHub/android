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

@HiltViewModel
class PasswordRegistrationViewModel
@Inject
constructor(
    private val postPasswordRegistrationUseCase: PostPasswordRegistrationUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(PasswordRegistrationUiState())
    val uiState: StateFlow<PasswordRegistrationUiState> = _uiState.asStateFlow()

    fun postPasswordRegistration(email: String, password: String) {
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