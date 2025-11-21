package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/** ---------------------------------------------------------------------------------------------- *
 * SupplyBatchSpecsDto -> Data class that has all the attributes that a supply batch is characterized
 * of.
 *
 * @param cantidad: int -> quantity of the supply batch
 * @param FechaCaducidad: String -> expiration date of the supply batch
 * @param adquisicion: String -> acquirement type of the supply batch
 * ---------------------------------------------------------------------------------------------- */
data class SupplyBatchSpecsDto(
    @SerializedName("idInventario") val idInventario: String? = null,
    /** Some APIs might return a direct `id` field for the batch */
    @SerializedName("id") val id: String? = null,
    /** Alternate possible key used by backend for batch id */
    @SerializedName("idLote") val idLote: String? = null,
    @SerializedName("cantidad") val quantity: Int?,
    @SerializedName("FechaCaducidad") val expirationDate: String?,
    @SerializedName("adquisicion") val adquisitionType: String?,
)
