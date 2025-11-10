package com.app.arcabyolimpo.data.remote.dto.password

import com.google.gson.annotations.SerializedName

data class UpdatePasswordDto(
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String
)