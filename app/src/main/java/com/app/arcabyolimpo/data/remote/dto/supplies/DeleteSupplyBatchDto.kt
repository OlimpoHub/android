package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object used to request the deletion of a specific supply batch.
 *
 * @property idSupply The unique identifier of the supply to which the batch belongs.
 * @property expirationDate The expiration date of the batch that should be deleted.
 */
data class DeleteSupplyBatchDto(
    @SerializedName("idInsumo") val idSupply: String,
    @SerializedName("fechaCaducidad") val expirationDate: String
)
