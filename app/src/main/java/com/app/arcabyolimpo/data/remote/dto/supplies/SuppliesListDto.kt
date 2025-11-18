package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a summarized supply item received from the API.
 *
 * @param id The unique identifier of the supply.
 * @param name The name of the supply.
 * @param image The URL or path of the supply image.
 */
data class SuppliesListDto(
    @SerializedName("idInsumo") val id: String,
    @SerializedName("nombre") val name: String,
    @SerializedName("imagenInsumo") val image: String?
)
