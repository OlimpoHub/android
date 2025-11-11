package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyBatchSpecsDto(
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("FechaCaducidad") val expirationDate: String,
    @SerializedName("adquisicion") val adquisitionType: String,
)
