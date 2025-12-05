package com.app.arcabyolimpo.presentation.screens.qr.scanresult

import com.app.arcabyolimpo.domain.model.qr.QrResult

/**
 * UI state model representing the current state of the QR scan result screen.
 *
 * This state is observed by the UI to display the outcome of the QR scan
 * process, including loading indicators, successful scan responses,
 * or error messages when something goes wrong.
 *
 * @property response Optional result returned after a successful QR scan.
 * @property isLoading Indicates whether the scan result is currently being processed.
 * @property error Optional message describing an error that occurred during the scan.
 */

data class ScanResultUiState(
    val response: QrResult? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)
