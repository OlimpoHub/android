package com.app.arcabyolimpo.data.remote.dto.ExternalCollaborator.RegisterExtCollab

import com.google.gson.annotations.SerializedName

data class RegisterResponseDto(
    @SerializedName("status")
    val status: Boolean,

    @SerializedName("message")
    val message: String
)


