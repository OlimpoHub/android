@file:Suppress("ktlint:standard:filename")

package com.app.arcabyolimpo.data.remote.dto.qr

import com.google.gson.annotations.SerializedName

data class CreateQrDto(
    @SerializedName("userID") val userID: String,
    @SerializedName("workshopID") val workshopID: String,
)
