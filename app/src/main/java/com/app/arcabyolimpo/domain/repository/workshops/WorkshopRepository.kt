package com.app.arcabyolimpo.domain.repository.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop

interface WorkshopRepository {

    suspend fun getWorkshopsList(): List<Workshop>

    suspend fun getWorkshopsById(id: String): Workshop

    suspend fun addWorkshop(newWorkshop: WorkshopDto): Workshop

    suspend fun modifyWorkshops(modifiedWorkshop: WorkshopDto): Workshop

    companion object

}