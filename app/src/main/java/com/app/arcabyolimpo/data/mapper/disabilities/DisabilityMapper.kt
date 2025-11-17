package com.app.arcabyolimpo.data.mapper.disabilities

import com.app.arcabyolimpo.data.remote.dto.disabilities.DisabilityDto
import com.app.arcabyolimpo.domain.model.disabilities.Disability

/**
 *  Extension function to convert a [DisabilityDto] from the data (remote) layer
 *  into a [Disability] domain model. This mapping ensures the domain layer remains
 *  independent from data transfer object (DTO) structures.
 */

fun DisabilityDto.toDomain(): Disability {
    return Disability (
        id = id,
        name = name,
        characteristics = characteristics
    )
}