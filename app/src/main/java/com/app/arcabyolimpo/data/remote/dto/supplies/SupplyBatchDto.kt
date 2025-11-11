package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyBatchDto(
    @SerializedName("idInsumo") val idSupply: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("imagenInsumo") val imageUrl: String,
    @SerializedName("unidadMedida") val unitMeasure: String,
    @SerializedName("cantidad") val totalQuantity: Int,
    @SerializedName("nombreTaller") val workshop: String,
    @SerializedName("Descripcion") val category: String,
    @SerializedName("status") val status: Int,
    @SerializedName("supplyBatchJson") val supplyBatches: List<SupplyBatchSpecsDto>?,
)
