package com.app.arcabyolimpo.data.remote.dto.beneficiaries

data class BeneficiaryFormData (
    val id: String? = null,
    val nombre: String = "",
    val apellidoPaterno: String = "",
    val apellidoMaterno: String = "",
    val fechaNacimiento: String = "",
    val numeroEmergencia: String = "",
    val nombreContactoEmergencia: String = "",
    val relacionContactoEmergencia: String = "",
    val descripcion: String = "",
    val fechaIngreso: String = "",
    val foto: String = "",
    val disabilities: List<String> = emptyList(),
    val estatus: Int = 1
)