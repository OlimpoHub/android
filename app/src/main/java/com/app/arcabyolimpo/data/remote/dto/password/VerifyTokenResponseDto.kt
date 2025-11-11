package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class VerifyTokenResponseDto(
    @SerializedName("valid") val valid: Boolean,
    @SerializedName("email") val email: String,
    @SerializedName("message") val message: String,
)
