package com.app.arcabyolimpo.data.remote.dto.beneficiaries

import com.google.gson.annotations.SerializedName

/**
 * DTO representing detailed information the beneficiary data before being overwritten.
 *
 * @param id The unique identifier of the beneficiary.
 * @param image The url of the image to be upload.
 * @param name The name of the new beneficiary.
 */
data class BeneficiariesListDto (
    @SerializedName("idBeneficiario") val id: String?,
    @SerializedName("nombreBeneficiario") val name: String?,
    @SerializedName("foto") val image: String?
)