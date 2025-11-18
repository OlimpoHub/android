package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

data class GetFilterBatchDto (
    @SerializedName("TipoAdquisicion") val acquisitionType: List<String>? = null,
    @SerializedName("FechaCaducidad") val expirationDate: List<String>? = null,
    @SerializedName("TotalCantidad") val quantity: List<String>? = null,
)