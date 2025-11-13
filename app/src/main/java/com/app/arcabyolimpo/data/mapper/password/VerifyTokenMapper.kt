package com.app.arcabyolimpo.data.mapper.password

import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.domain.model.password.VerifyToken

/**
 * Maps a [VerifyTokenResponseDto] object from the data layer to a [VerifyToken] domain model.
 *
 * This function converts the API response for token verification into a domain-friendly
 * representation, making it easier for the app’s business logic to process.
 *
 * @receiver The [VerifyTokenResponseDto] returned by the API.
 * @return A [VerifyToken] domain model containing the mapped verification data.
 */

fun VerifyTokenResponseDto.toDomain(): VerifyToken =
    VerifyToken(
        valid = valid,
        email = email,
        message = message,
    )
