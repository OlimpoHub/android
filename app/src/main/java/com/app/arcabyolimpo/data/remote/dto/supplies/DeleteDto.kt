package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

// You only define the data parameters
data class DeleteDto (
    @SerializedName("id") val id: String
)