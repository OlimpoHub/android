package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class RecoverPasswordResponseDto(
    @SerializedName("message") val message: String
)

