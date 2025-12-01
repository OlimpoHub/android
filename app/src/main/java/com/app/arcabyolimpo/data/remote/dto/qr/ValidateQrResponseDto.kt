package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the API response for QR code validation.
 *
 * This class contains the message returned by the backend after attempting to
 * validate a scanned QR code. The message typically indicates whether the QR
 * was valid, invalid, expired, or if any other condition was triggered.
 *
 * @property message The result message provided by the API after validation.
 */
data class ValidateQrResponseDto(
    @SerializedName("message") val message: String,
)
