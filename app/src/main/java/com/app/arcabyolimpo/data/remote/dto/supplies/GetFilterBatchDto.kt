package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class GetFilterBatchDto (
    @SerializedName("acquisitionTypes") val acquisitionType: List<String>? = null,
    @SerializedName("expirationDates") val expirationDate: List<String>? = null,
)