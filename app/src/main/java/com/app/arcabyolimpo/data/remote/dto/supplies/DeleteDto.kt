package com.app.arcabyolimpo.data.remote.dto.supplies

import com.google.gson.annotations.SerializedName

// solo defines los parametros de los datos
data class DeleteDto (
    @SerializedName("id") val id: String
)