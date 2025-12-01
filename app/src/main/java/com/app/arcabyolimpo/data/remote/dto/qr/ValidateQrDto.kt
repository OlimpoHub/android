package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) used to validate a scanned QR code through the API.
 *
 * This class represents the payload sent to the backend after a QR code is scanned,
 * containing the scanned value, the timestamp when it was read, and the ID of the
 * user performing the scan.
 *
 * @property qrValue The decoded value of the scanned QR code. May be null if the scan failed.
 * @property readTime The UNIX timestamp (in milliseconds) indicating when the QR was scanned.
 * @property userID The unique identifier of the user validating the QR code.
 */
data class ValidateQrDto(
    @SerializedName("qrValue") val qrValue: String?,
    @SerializedName("readTime") val readTime: Long,
    @SerializedName("userID") val userID: String,
)
