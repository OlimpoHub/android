package com.app.arcabyolimpo.data.remote.dto.user.updateuser

import com.google.gson.annotations.SerializedName

data class UpdateResponseDto(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


