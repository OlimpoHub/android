package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

data class ValidateQrResponseDto(
    @SerializedName("message") val message: String,
)
