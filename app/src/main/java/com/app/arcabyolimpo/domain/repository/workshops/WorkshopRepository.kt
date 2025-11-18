package com.app.arcabyolimpo.domain.repository.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop

interface WorkshopRepository {

    suspend fun getWorkshopsList(): List<Workshop>

    suspend fun getWorkshopsById(id: String): Workshop?

    suspend fun addWorkshop(newWorkshop: WorkshopDto): Workshop

    suspend fun searchWorkshop(name: String): List<Workshop>

    /**
     * Deletes a single workshop identified by its [id].
     *
     * A simple function so that the domain or presentation layer
     * can request the deletion of an Workshop.
     *
     * @param id Unique identifier of the workshop to be deleted.
     */
    suspend fun deleteWorkshops(id: String)

    companion object

}