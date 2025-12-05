@file:Suppress("ktlint:standard:filename")

package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) used to request QR code creation from the API.
 *
 * This class represents the payload required by the backend to generate a new
 * QR code associated with a specific user and workshop.
 *
 * @property userID The unique identifier of the user for whom the QR is being created.
 * @property workshopID The identifier of the workshop tied to the generated QR code.
 */
data class CreateQrDto(
    @SerializedName("userID") val userID: String,
    @SerializedName("workshopID") val workshopID: String,
)
