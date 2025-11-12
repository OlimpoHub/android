package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) representing the API response for token verification.
 *
 * @property valid Indicates whether the provided token is valid.
 * @property email The email address associated with the verified token.
 * @property message A message from the API describing the verification result.
 */

data class VerifyTokenResponseDto(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("email") val email: String,
    @SerializedName("message") val message: String,
)
