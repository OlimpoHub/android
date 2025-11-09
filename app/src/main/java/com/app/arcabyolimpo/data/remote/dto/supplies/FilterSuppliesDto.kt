package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class FilterSuppliesDto(
    @SerializedName("categories") val categories: List<String>? = null,
    @SerializedName("measures") val measures: List<String>? = null,
    @SerializedName("workshops") val workshops: List<String>? = null,
)
