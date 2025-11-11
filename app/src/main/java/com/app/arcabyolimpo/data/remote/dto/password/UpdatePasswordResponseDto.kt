package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class UpdatePasswordResponseDto(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
)
