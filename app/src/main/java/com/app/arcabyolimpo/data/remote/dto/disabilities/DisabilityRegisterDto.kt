package com.app.arcabyolimpo.data.remote.dto.disabilities

/**
 * Data Transfer Object (DTO) for registering a new disability.
 *
 * This class models the JSON payload sent to the API endpoint when creating
 * a new disability entry.
 *
 * @param nombre The name of the disability.
 * @param descripcion A description of the disability's characteristics.
 */
data class DisabilityRegisterDto(
    val nombre: String,
    val descripcion: String,
)
