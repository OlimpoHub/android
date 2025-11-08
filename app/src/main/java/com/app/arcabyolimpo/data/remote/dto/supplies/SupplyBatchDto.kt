package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyBatchDto(
    @SerializedName("cantidad") val quantity: Int,
    @SerializedName("FechaCaducidad") val expirationDate: String,
)
