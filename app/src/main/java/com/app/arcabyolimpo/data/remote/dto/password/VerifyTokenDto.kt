package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class VerifyTokenDto (
    @SerializedName("token") val token: String
) {}