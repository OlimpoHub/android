package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

data class WorkshopsListDto (
    @SerializedName("idTaller") val id: String,
    @SerializedName("URL") val image: String,
    @SerializedName("nombreTaller") val name: String
)