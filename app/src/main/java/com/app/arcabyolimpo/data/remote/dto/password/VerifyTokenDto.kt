package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object (DTO) used to send a verification token to the API.
 *
 * @property token The unique verification token associated with a user's account or password reset request.
 */

data class VerifyTokenDto(
    @SerializedName("token") val token: String,
)
