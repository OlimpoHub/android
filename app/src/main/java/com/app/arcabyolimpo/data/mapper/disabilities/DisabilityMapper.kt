package com.app.arcabyolimpo.data.mapper.disabilities

import com.app.arcabyolimpo.data.remote.dto.disabilities.DisabilityDto
import com.app.arcabyolimpo.data.remote.dto.disabilities.DisabilityRegisterDto
import com.app.arcabyolimpo.domain.model.disabilities.Disability

/**
 *  Extension function to convert a [DisabilityDto] from the data (remote) layer
 *  into a [Disability] domain model. This mapping ensures the domain layer remains
 *  independent from data transfer object (DTO) structures.
 */

fun DisabilityDto.toDomain(): Disability =
    Disability(
        id = id,
        name = name,
        characteristics = characteristics,
    )

/**
 * Converts a [Disability] (domain layer) to a [DisabilityRegisterDto] (data layer).
 *
 * This mapping is used specifically for the creation of a new disability, preparing
 * the data to be sent to the remote API.
 *
 * @return A [DisabilityRegisterDto] ready for network serialization.
 */
fun Disability.toRegisterDto(): DisabilityRegisterDto =
    DisabilityRegisterDto(
        nombre = name,
        descripcion = characteristics,
    )
