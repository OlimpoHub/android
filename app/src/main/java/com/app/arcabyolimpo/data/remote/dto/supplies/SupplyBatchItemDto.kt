package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyBatchItemDto(
    @SerializedName("id") val id: String,
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("expirationDate") val expirationDate: String,
    @SerializedName("adquisitionType") val adquisitionType: String,
    @SerializedName("boughtDate") val boughtDate: String,
    @SerializedName("measure") val measure: String,
)
