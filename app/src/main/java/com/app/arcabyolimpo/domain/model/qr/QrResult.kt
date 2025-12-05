package com.app.arcabyolimpo.domain.model.qr

/**
 * Represents the result of a QR scan in the domain layer.
 *
 * This model encapsulates the decoded value extracted from a scanned QR code.
 * It is used by domain use cases and passed to presentation layers to handle
 * validation, processing, or navigation flows.
 *
 * @property value The decoded content of the scanned QR code.
 */
data class QrResult(
    val value: String,
)
