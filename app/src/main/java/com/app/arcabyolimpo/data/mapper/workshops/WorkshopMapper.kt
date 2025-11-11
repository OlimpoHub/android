package com.app.arcabyolimpo.data.mapper.workshops

import com.app.arcabyolimpo.data.remote.dto.supplies.SupplyDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.workshops.Workshop

/**
 * Extension function to convert a [WorkshopDto] from the data (remote) layer
 * into a [Workshop] domain model. This mapping ensures the domain layer remains
 * independent from data transfer object (DTO) structures.
 */
fun WorkshopDto.toDomain(): Workshop {
    return Workshop(
        id = id, /** id of the workshop, it is a uuid */
        idTraining = idTraining,
        idUser = idUser,
        nameWorkshop = name.replaceFirstChar { it.uppercase() },
        url = image,
        startHour = startHour,
        finishHour = finishHour,
        status = status,
        description = description,
        date = date

    )
}