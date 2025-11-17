package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class DeleteSupplyBatchDto(
    @SerializedName("idInsumo") val idSupply: String,
    @SerializedName("fechaCaducidad") val expirationDate: String?
)