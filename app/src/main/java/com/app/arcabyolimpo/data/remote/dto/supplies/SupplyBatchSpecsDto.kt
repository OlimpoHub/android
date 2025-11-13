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
    @SerializedName("cantidad") val quantity: Int?,
    @SerializedName("FechaCaducidad") val expirationDate: String?,
    @SerializedName("adquisicion") val adquisitionType: String?,
)
