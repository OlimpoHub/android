package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information of a supply received from the API.
 *
 * @param id The unique identifier of the supply.
 * @param image The URL or path of the supply image.
 * @param name The name of the supply.
 * @param unitMeasure The measurement unit used for the supply.
 * @param batch The list of [SupplyBatchDto] associated with the supply.
 */
data class SupplyDto(
    @SerializedName("idInsumo") val id: String,
    @SerializedName("imagenInsumo") val image: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("unidadMedida") val unitMeasure: String,
    @SerializedName("supplyBatchJson") val batch: List<SupplyBatchDto>
)
