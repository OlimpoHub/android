package com.app.arcabyolimpo.presentation.screens.qr.scanqr

import com.app.arcabyolimpo.domain.model.qr.QrResult

/**
 * UI state model representing the current state of the QR scanning screen.
 *
 * This state is observed by the UI to reflect updates produced during the
 * scanning and processing of a QR code, such as loading status, successful
 * scan results, or context-specific errors.
 *
 * @property response Optional QR result returned when the scan is successfully processed.
 * @property isLoading Indicates whether the QR scan or validation request is currently in progress.
 * @property error Optional error message displayed when the scan or validation fails.
 */

data class ScanQrUiState(
    val response: QrResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
