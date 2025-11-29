package com.app.arcabyolimpo.data.remote.dto.beneficiaries

import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information of a beneficiary received from the API.
 *
 * @param id The unique identifier of the beneficiary.
 * @param firstName The first name of the beneficiary.
 * @param paternalName The paternal name (surname) of the beneficiary.
 * @param maternalName The maternal name (surname) of the beneficiary.
 * @param birthdate The birthdate of the beneficiary.
 * @param emergencyNumber The emergency contact number of the beneficiary.
 * @param emergencyName The name of the beneficiary's emergency contact.
 * @param emergencyRelation The relationship of the beneficiary with their emergency contact.
 * @param details Additional details about the beneficiary.
 * @param entryDate The date when the beneficiary entered the system.
 * @param image The url of the image to be upload.
 * @param disabilities Any specific disabilities the beneficiary has.
 * @param status The status of the beneficiary, active = 1, inactive = 0.
 */
data class BeneficiaryDto(
    @SerializedName("idBeneficiario") val id: String?,
    @SerializedName("nombre") val firstName: String?,
    @SerializedName("apellidoPaterno") val paternalName: String?,
    @SerializedName("apellidoMaterno") val maternalName: String?,
    @SerializedName("fechaNacimiento") val birthdate: String?,
    @SerializedName("numeroEmergencia") val emergencyNumber: String?,
    @SerializedName("nombreContactoEmergencia") val emergencyName: String?,
    @SerializedName("relacionContactoEmergencia") val emergencyRelation: String?,
    @SerializedName("descripcion") val details: String?,
    @SerializedName("fechaIngreso") val entryDate: String?,
    @SerializedName("foto") val image: String?,
    @SerializedName("discapacidades") val disabilities: List<String>?,
    @SerializedName("estatus") val status: Int?
)