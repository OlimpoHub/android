package com.app.arcabyolimpo.data.mapper.password

import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.domain.model.password.UpdatePassword

/**
 * Maps an [UpdatePasswordResponseDto] object from the data layer to a [UpdatePassword] domain model.
 *
 * This transformation is used to convert raw API response data into a format
 * suitable for use in the domain layer.
 *
 * @receiver The [UpdatePasswordResponseDto] received from the API.
 * @return A [UpdatePassword] domain model containing the mapped data.
 */

fun UpdatePasswordResponseDto.toDomain(): UpdatePassword =
    UpdatePassword(
        status = status,
        message = message,
    )
