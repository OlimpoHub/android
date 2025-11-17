package com.app.arcabyolimpo.data.remote.dto.beneficiaries

/**
 * DTO representing detailed information of the information when the workshop is added.
 *
 * @param success If the workshop was added successfully.
 * @param message The message shown when the workshop was added successfully.
 * @param idBeneficiary The unique identifier of the beneficiary.
 */

data class AddNewBeneficiaryDto (
    val success: Boolean,
    val message: String,
    val idBeneficiary: String?
)