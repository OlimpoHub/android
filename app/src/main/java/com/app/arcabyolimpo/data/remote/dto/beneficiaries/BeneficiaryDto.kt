package com.app.arcabyolimpo.data.remote.dto.beneficiaries

import com.google.gson.annotations.SerializedName

data class BeneficiaryDto (
    @SerializedName("idBeneficiario") val id: String,
    @SerializedName("nombreBeneficiario") val name: String,
    @SerializedName("fechaNacimiento") val birthdate: String,
    @SerializedName("numeroContactoEmergencia") val emergencyNumber: String,
    @SerializedName("nombreContactoEmergencia") val emergencyName: String,
    @SerializedName("relacionContactoEmergencia") val emergencyRelation: String,
    @SerializedName("descripcion") val details: String,
    @SerializedName("fechaIngreso") val entryDate: String,
    @SerializedName("foto") val image: String,
    @SerializedName("discapacidad") val disabilities: String,
    @SerializedName("estatus") val status: Int
)