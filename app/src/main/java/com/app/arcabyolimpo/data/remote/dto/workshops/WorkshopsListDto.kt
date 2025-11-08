package com.app.arcabyolimpo.data.remote.dto.workshops

import com.google.gson.annotations.SerializedName

data class WorkshopsListDto (
    @SerializedName("idTaller") val id: String,
    @SerializedName("URL") val image: String,
<<<<<<< HEAD
    @SerializedName("nombreTaller") val name: String,
=======
    @SerializedName("nombreTaller") val name: String
>>>>>>> e5fc8f949adf43bfa4022a328cb3944f2d60ebba
)