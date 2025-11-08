package com.app.arcabyolimpo.domain.repository.workshops

import com.app.arcabyolimpo.domain.model.workshops.Workshop

interface WorkshopRepository {

    suspend fun getWorkshopsList(): List<Workshop>

    suspend fun getWorkshopById(id: String): Workshop
}