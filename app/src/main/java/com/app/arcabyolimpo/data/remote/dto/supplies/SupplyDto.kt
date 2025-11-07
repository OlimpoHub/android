package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyDto(
    @SerializedName("idInsumo") val id: String,
    @SerializedName("imagenInsumo") val image: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("unidadMedida") val unitMeasure: String,
    @SerializedName("supplyBatchJson") val batch: List<SupplyBatchDto>
)
