package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a batch of a supply item received from the API.
 *
 * @param idInsumo: String -> id of the supply
 * @param nombre: String -> name of the supply
 * @param imagenInsumo: String -> image of the supply
 * @param unidadMedida: String -> measure unit of the supply
 * @param cantidad: Int -> quantity of the supply
 * @param nombreTaller: String -> Workshop where the supply is being used
 * @param Descripcion: String -> category of the supply
 * @param status: Int -> status of the supply
 * @param SupplyBatchJson: List<SupplyBatchSpecsDto> -> attributes from the supply batch
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
