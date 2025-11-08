package com.app.arcabyolimpo.data.mapper.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop

fun WorkshopDto.toDomain(): Workshop {
    return Workshop(
        id = id,
        nameWorkshop = name.replaceFirstChar { it.uppercase() },
        url = image,
        startHour = starthour,
        finishHour = finishhour,
        status = status,
        schedule = schedule,
        date = date

    )
}