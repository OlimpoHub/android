package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class DeleteResponseDto (
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
)