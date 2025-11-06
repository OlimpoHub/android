package com.app.arcabyolimpo.presentation.screens.tokenverification

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.password.GetVerifyTokenUseCase
import com.app.arcabyolimpo.domain.usecase.password.PostPasswordRecoveryUseCase
import com.app.arcabyolimpo.presentation.screens.passwordrecovery.PasswordRecoveryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TokenVerificationViewModel
@Inject
constructor(
    private val getVerifyTokenUseCase: GetVerifyTokenUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TokenVerificationUiState())
    val uiState: StateFlow<TokenVerificationUiState> = _uiState.asStateFlow()

    fun getTokenVerification(token: String) {
        viewModelScope.launch {
            getVerifyTokenUseCase(token).collect { result ->
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