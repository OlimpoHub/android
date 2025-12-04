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

/**
 * ViewModel responsible for managing the QR generation flow for workshop attendance.
 *
 * This ViewModel communicates with the domain layer through [PostCreateQrUseCase] to
 * request the creation of a QR code associated with a specific workshop and the
 * authenticated user. It exposes a [StateFlow] of [QrUiState] that the UI observes
 * to render loading, success, or error states.
 *
 * When a QR is successfully generated, the ViewModel decodes the returned byte array
 * into a `Bitmap`, updates the state, and makes it available for display in the UI layer.
 *
 * ## Responsibilities
 * - Retrieve the current user ID from [UserPreferences].
 * - Trigger QR creation using the provided workshop ID.
 * - Handle `Loading`, `Success`, and `Error` results emitted by the use case.
 * - Update the UI state through a single source of truth: [_uiState].
 *
 * @property postCreateQrUseCase Use case that performs the QR generation request.
 * @property userPreferences Local storage that provides the authenticated user's ID.
 */

@HiltViewModel
class QrViewModel
    @Inject
    constructor(
        private val postCreateQrUseCase: PostCreateQrUseCase,
        private val userPreferences: UserPreferences,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(QrUiState())
        val uiState: StateFlow<QrUiState> = _uiState.asStateFlow()

        /**
         * Sends a QR creation request based on the given workshop ID, associating it
         * with the currently authenticated user.
         *
         * ### Steps performed:
         * 1. Retrieves the user ID from [UserPreferences].
         * 2. Calls the [PostCreateQrUseCase] with the user ID and workshop ID.
         * 3. Collects the emitted [Result] states:
         *    - **Loading:** Flags the UI to show a progress indicator.
         *    - **Success:** Decodes the byte array into a Bitmap and updates UI state.
         *    - **Error:** Stores the error message and stops loading.
         *
         * @param workShopId ID of the workshop for which the QR code is requested.
         */
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
