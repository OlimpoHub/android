package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class RecoverPasswordDto (
    @SerializedName("email") val email: String
) {}