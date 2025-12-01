package com.app.arcabyolimpo.presentation.screens.qr.qr

import android.graphics.Bitmap

/**
 * UI state container for the QR generation screen.
 *
 * This data class represents the different states produced by the QR creation flow,
 * including the generated QR bitmap, loading indicator, and potential error messages.
 * It is observed by the UI layer to render the correct visual feedback depending
 * on the current stage of the operation.
 *
 * @property qrBitmap: Holds the generated QR image when the operation succeeds.
 * @property isLoading: Indicates whether the QR creation request is currently in progress.
 * @property error: Provides context-specific error information when the QR cannot be generated.
 *
 * Used by [QrViewModel] to expose reactive UI state updates to the `QrScreen`.
 */

data class QrUiState(
    val qrBitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
