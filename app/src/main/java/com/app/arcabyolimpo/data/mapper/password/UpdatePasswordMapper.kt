package com.app.arcabyolimpo.data.mapper.password

import com.app.arcabyolimpo.data.remote.dto.password.UpdatePasswordResponseDto
import com.app.arcabyolimpo.domain.model.password.UpdatePassword

fun UpdatePasswordResponseDto.toDomain(): UpdatePassword =
    UpdatePassword(
        status = status,
        message = message,
    )
