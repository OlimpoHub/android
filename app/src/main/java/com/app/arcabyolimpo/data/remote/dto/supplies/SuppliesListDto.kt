package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SuppliesListDto(
    @SerializedName("idInsumo") val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("imagenInsumo") val image: String?
)
