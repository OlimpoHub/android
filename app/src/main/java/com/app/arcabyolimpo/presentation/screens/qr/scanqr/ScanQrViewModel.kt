package com.app.arcabyolimpo.presentation.screens.qr.scanqr

import android.content.Context
import androidx.camera.view.PreviewView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.app.arcabyolimpo.domain.common.Result
import com.app.arcabyolimpo.domain.usecase.qr.PostScanQrUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanQrViewModel
    @Inject
    constructor(
        private val postScanQrUseCase: PostScanQrUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ScanQrUiState())
        val uiState: StateFlow<ScanQrUiState> = _uiState.asStateFlow()

        fun postScanQr(context: Context) {
            viewModelScope.launch {
                postScanQrUseCase(context).collect { result ->
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

        fun startCamera(
            previewView: PreviewView,
            lifecycleOwner: LifecycleOwner,
        ) {
            viewModelScope.launch {
                postScanQrUseCase.startCamera(previewView, lifecycleOwner)
            }
        }

        fun stopScanning() {
            viewModelScope.launch {
                postScanQrUseCase.stopScanning()
            }
        }
    }
