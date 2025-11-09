package com.app.arcabyolimpo.data.mapper.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop

fun WorkshopDto.toDomain(): Workshop {
    return Workshop(
        id = id, //id del taller que se genera en uuid
        idTraining = idTraining,
        idUser = idUser,
        nameWorkshop = name.replaceFirstChar { it.uppercase() },
        url = image,
        startHour = startHour,
        finishHour = finishHour,
        status = status,
        schedule = schedule,
        date = date

    )
}