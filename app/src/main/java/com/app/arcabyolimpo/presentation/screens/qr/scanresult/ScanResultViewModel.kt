package com.app.arcabyolimpo.presentation.screens.qr.scanresult

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.qr.PostValidateQrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanResultViewModel
    @Inject
    constructor(
        private val postValidateQrUseCase: PostValidateQrUseCase,
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ScanResultUiState())
        val uiState: StateFlow<ScanResultUiState> = _uiState.asStateFlow()

        fun postScanResult(
            qrValue: String?,
            readTime: Long,
        ) {
            viewModelScope.launch {
                postValidateQrUseCase(
                    qrValue,
                    readTime,
                    userPreferences.getUserId().first().orEmpty(),
                ).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success -> {
                                state.copy(
                                    response = result.data,
                                    isLoading = false,
                                    error = null,
                                )
                            }
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
