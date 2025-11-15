package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SuccessMessageDto(
    @SerializedName("message") val message: String,
)
