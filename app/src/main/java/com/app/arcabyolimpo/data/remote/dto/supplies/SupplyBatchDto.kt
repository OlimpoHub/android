package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a batch of a supply item received from the API.
 *
 * @param quantity The amount of units in the batch.
 * @param expirationDate The expiration date associated with the batch.
 */
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
