package com.app.arcabyolimpo.domain.model.disabilities

/**
 * Domain model representing a disability in the system.
 *
 * @param id The unique identifier of the disability.
 * @param name The name of the disability
 * @param characteristics The characteristics from the disability
 */

data class Disability(
    val id: String,
    val name: String,
    val characteristics: String
)