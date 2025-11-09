package com.app.arcabyolimpo.data.mapper.password

import com.app.arcabyolimpo.data.remote.dto.password.VerifyTokenResponseDto
import com.app.arcabyolimpo.domain.model.password.VerifyToken

fun VerifyTokenResponseDto.toDomain(): VerifyToken =
    VerifyToken(
        valid = valid,
        email = email,
        message = message
    )