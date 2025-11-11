package com.app.arcabyolimpo.domain.model.beneficiaries

/**
 * Domain model representing a beneficiary in the system.
 *
 * @property id The unique identifier of the beneficiary.
 * @property name The name of the new beneficiary.
 * @property birthdate The birthdate of the beneficiary.
 * @property emergencyNumber The emergency contact number of the beneficiary.
 * @property emergencyName The name of the beneficiary's emergency contact.
 * @property emergencyRelation The relationship of the beneficiary with their emergency contact.
 * @property details Additional details about the beneficiary.
 * @property entryDate The date when the beneficiary entered the system.
 * @property image The url of the image to be upload.
 * @property disabilities Any specific disabilities the beneficiary has.
 * @property status The status of the beneficiary, active = 1, inactive = 0.
 */
data class Beneficiary(
    val id: String,
    val name: String,
    val birthdate: String,
    val emergencyNumber: String,
    val emergencyName: String,
    val emergencyRelation: String,
    val details: String,
    val entryDate: String,
    val image: String,
    val disabilities: String,
    val status: Int
)