package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class SupplyBatchListDto(
    @SerializedName("batch") val batch: List<SupplyBatchItemDto>,
)
