package com.app.arcabyolimpo.data.mapper.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopFormData
import com.app.arcabyolimpo.domain.model.workshops.Workshop

/**
 * Converts a [WorkshopDto] (from API) to a [Workshop] (domain model).
 */
fun WorkshopDto.toDomain(): Workshop {
    return Workshop(
        id = id ?: "",
        idUser = idUser ?: "",
        nameWorkshop = (name ?: "").replaceFirstChar { it.uppercase() },
        url = image ?: "",
        startHour = startHour ?: "",
        finishHour = finishHour ?: "",
        status = status,
        description = description ?: "",
        date = date ?: "",
        videoTraining = videoTraining ?: ""
    )
}

/**
 * Converts a [WorkshopFormData] (form submission) to a [WorkshopDto] (for API).
 */
fun WorkshopFormData.toWorkshopDto() = WorkshopDto(
    id = id ?: "",
    idUser = idUser ?: "",
    name = name ?: "",
    startHour = startHour ?: "",
    finishHour = finishHour ?: "",
    description = description ?: "",
    date = date ?: "",
    image = image ?: "",
    videoTraining = videoTraining ?: "",
    status = 1
)


/**
 * Converts a [WorkshopFormData] directly to a [Workshop] (domain model).
 * Useful for showing previews or local data handling.
 */
fun WorkshopFormData.toDomain(): Workshop {
    return Workshop(
        id = id,
        idUser = idUser,
        nameWorkshop = name.replaceFirstChar { it.uppercase() },
        url = image,
        startHour = startHour,
        finishHour = finishHour,
        status = status,
        description = description,
        date = date,
        videoTraining = videoTraining
    )
}
