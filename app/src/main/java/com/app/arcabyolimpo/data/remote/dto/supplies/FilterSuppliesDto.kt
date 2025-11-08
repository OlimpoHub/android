package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class FilterSuppliesDto(
    @SerializedName("type") val type: String,
    @SerializedName("value") val value: String,
)
