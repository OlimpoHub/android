package com.app.arcabyolimpo.data.remote.dto.beneficiaries

import com.google.gson.annotations.SerializedName

data class BeneficiariesListDto (
    @SerializedName("idBeneficiario") val id: String,
    @SerializedName("nombreBeneficiario") val name: String,
    @SerializedName("foto") val image: String
)