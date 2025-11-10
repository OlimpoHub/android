package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * DTO representing a batch of a supply item received from the API.
 *
 * @param quantity The amount of units in the batch.
 * @param expirationDate The expiration date associated with the batch.
 */
data class SupplyBatchDto(
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("FechaCaducidad") val expirationDate: String
)
