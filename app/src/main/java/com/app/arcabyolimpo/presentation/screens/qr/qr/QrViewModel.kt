package com.app.arcabyolimpo.presentation.screens.qr.qr

import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.arcabyolimpo.data.local.auth.UserPreferences
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.qr.PostCreateQrUseCase
import com.app.arcabyolimpo.presentation.screens.qr.qr.QrUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QrViewModel
    @Inject
    constructor(
        private val postCreateQrUseCase: PostCreateQrUseCase,
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QrUiState())
        val uiState: StateFlow<QrUiState> = _uiState.asStateFlow()

        fun postCreateQr(workShopId: String) {
            viewModelScope.launch {
                postCreateQrUseCase(userPreferences.getUserId().first()!!, workShopId).collect { result ->
                    _uiState.update { state ->
                        when (result) {
                            is Result.Loading ->
                                state.copy(
                                    isLoading = true,
                                )
                            is Result.Success -> {
                                val bytes = result.data
                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)

                                state.copy(
                                    qrBitmap = bitmap,
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
