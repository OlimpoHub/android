package com.app.arcabyolimpo.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.auth.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val loginUseCase: LoginUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(LoginUiState())
        val uiState = _uiState.asStateFlow()

        fun updateUsername(value: String) {
            _uiState.update {
                it.copy(
                    username = value,
                    error = null,
                )
            }
        }

        fun updatePassword(value: String) {
            _uiState.update {
                it.copy(
                    password = value,
                    error = null,
                )
            }
        }

        fun login() {
            val username = _uiState.value.username
            val password = _uiState.value.password

            viewModelScope.launch {
                loginUseCase(username, password).collect { result ->
                    when (result) {
                        is Result.Loading -> {
                            _uiState.update { it.copy(isLoading = true, error = null) }
                        }
                        is Result.Success -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    user = result.data,
                                    error = null,
                                )
                            }
                        }
                        is Result.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message ?: "Error desconocido",
                                )
                            }
                        }
                    }
                }
            }
        }
    }
