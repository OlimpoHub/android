package com.app.arcabyolimpo.domain.model.beneficiaries

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