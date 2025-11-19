package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

/**
 * Data Transfer Object representing a batch returned from the filter API response.
 *
 * @property id The inventory batch ID.
 * @property idSupply The ID of the supply this batch belongs to.
 * @property adquisitionType The acquisition type associated with the batch.
 * @property expirationDate The expiration date of the batch.
 * @property quantity Total quantity available for this specific batch.
 */
data class FilteredBatchDto(
    @SerializedName("idInventario") val id: String?,
    @SerializedName("idInsumo") val idSupply: String?,
    @SerializedName("TipoAdquisicion") val adquisitionType: String?,
    @SerializedName("FechaCaducidad") val expirationDate: String?,
    @SerializedName("TotalCantidad") val quantity: Int?,
)
