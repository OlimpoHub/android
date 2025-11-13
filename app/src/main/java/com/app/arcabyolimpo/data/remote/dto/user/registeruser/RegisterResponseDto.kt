package com.app.arcabyolimpo.data.remote.dto.user.registeruser

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


