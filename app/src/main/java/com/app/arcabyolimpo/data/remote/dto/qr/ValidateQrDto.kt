package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

data class ValidateQrDto(
    @SerializedName("qrValue") val qrValue: String?,
    @SerializedName("readTime") val readTime: Long,
    @SerializedName("userID") val userID: String,
)
