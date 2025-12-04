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

/**
 * ViewModel responsible for handling the QR scanning workflow and exposing UI state.
 *
 * This ViewModel coordinates interactions between the camera, the QR scanning use case,
 * and the UI layer. It updates and exposes a [ScanQrUiState] object that reflects the
 * current scanning status, loading state, errors, and backend responses.
 *
 * Responsibilities:
 * - Start and stop the camera for scanning.
 * - Trigger the QR scan request and emit loading, success, or error states.
 * - Manage one-time events such as scan responses through state consumption helpers.
 *
 * @property postScanQrUseCase Domain use case that handles posting scanned QR data
 * and managing camera lifecycle operations.
 *
 * Public functions:
 * @see postScanQr Sends the QR scan result to the backend and updates the UI state.
 * @see startCamera Starts camera streaming and prepares QR detection.
 * @see stopScanning Stops camera operations and prevents further QR reads.
 * @see clearScanResult Clears both response and error from the UI state.
 * @see consumeResponse Clears only the consumed response to avoid repeated triggers.
 */

@HiltViewModel
class ScanQrViewModel
    @Inject
    constructor(
        private val postScanQrUseCase: PostScanQrUseCase,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(ScanQrUiState())
        val uiState: StateFlow<ScanQrUiState> = _uiState.asStateFlow()

        /**
         * Sends the scanned QR data to the backend through [PostScanQrUseCase].
         *
         * ### Behavior:
         * - Emits **Result.Loading** → shows loading indicator.
         * - Emits **Result.Success** → stores backend message in UI state.
         * - Emits **Result.Error** → stores error message and stops loading.
         *
         * This function is typically triggered immediately after decoding
         * a valid QR code.
         *
         * @param context Android context required for decoding and permissions.
         */
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

        /**
         * Starts the camera preview and prepares the system for QR detection.
         *
         * Delegates to the use case which configures CameraX with the provided:
         * - [PreviewView] for rendering the camera feed,
         * - [LifecycleOwner] to handle binding/unbinding.
         *
         * Called when the user navigates to the scanning screen.
         *
         * @param previewView Camera feed preview container.
         * @param lifecycleOwner Owner controlling the camera lifecycle.
         */
        fun startCamera(
            previewView: PreviewView,
            lifecycleOwner: LifecycleOwner,
        ) {
            viewModelScope.launch {
                postScanQrUseCase.startCamera(previewView, lifecycleOwner)
            }
        }

        /**
         * Stops camera operations and prevents additional QR reads.
         *
         * Useful when:
         * - leaving the screen,
         * - a scan has been successfully processed,
         * - preventing duplicate scans.
         */
        fun stopScanning() {
            viewModelScope.launch {
                postScanQrUseCase.stopScanning()
            }
        }

        /**
         * Clears both the `response` and `error` from UI state.
         *
         * Used when the screen is reset, dismissed, or when the
         * UI should return to a neutral state.
         */
        fun clearScanResult() {
            _uiState.update {
                it.copy(response = null, error = null)
            }
        }

        /**
         * Consumes only the stored `response` to prevent showing it again.
         *
         * Ideal for one-time events such as:
         * - dialog messages,
         * - toast notifications,
         * - navigation triggers.
         *
         * The error remains intact unless explicitly cleared.
         */
        fun consumeResponse() {
            _uiState.update { it.copy(response = null) }
        }
    }
