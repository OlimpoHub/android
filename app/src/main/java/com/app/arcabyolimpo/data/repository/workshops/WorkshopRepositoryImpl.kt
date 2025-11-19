package com.app.arcabyolimpo.data.repository.workshops

import android.util.Log
import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
import com.app.arcabyolimpo.data.remote.dto.supplies.DeleteDto
import com.app.arcabyolimpo.data.remote.dto.workshops.DeleteWorkshopDto
import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop
import com.app.arcabyolimpo.domain.repository.workshops.WorkshopRepository

import javax.inject.Inject

/**
 * Implementation of [WorkshopRepository] that manages data retrieval
 * related to supplies from the remote API.
 *
 * This class uses [ArcaApi] to fetch workshop data and maps it into
 * domain models to maintain a clean separation between data and domain layers.
 */
class WorkshopRepositoryImpl
    @Inject constructor(
        private val api: ArcaApi
    ) : WorkshopRepository {

    /**
     * Retrieves a list of all available supplies from the API.
     *
     * Each item in the response is converted into a [Workshop] domain model.
     * Since this endpoint provides limited data, some fields like
     * [Workshop.status] or [Workshop.description] are initialized as empty.
     *
     * @return A list of [Workshop] objects representing the available supplies.
     */
    override suspend fun getWorkshopsList(): List<Workshop> {
        val response = api.getWorkshopsList()
        return response.map { dto ->
            Workshop(
                id = dto.id,
                idUser = dto.idUser,
                nameWorkshop = dto.name,
                url = dto.image,
                status = 0,
                description = "",
                startHour = "",
                finishHour = "",
                date = "",
                videoTraining = ""
            )
        }
    }

    /**
     * Retrieves detailed information for a specific workshop by its [id].
     *
     * This function calls the API to obtain the corresponding [WorkshopDto]
     * and converts it into a [Workshop] domain model using the [toDomain] mapper.
     *
     * @param id The unique identifier of the workshop to retrieve.
     * @return A [Workshop] object containing detailed workshop information.
     */
    override suspend fun getWorkshopsById(id: String): Workshop? {
        val response = api.getWorkshop(id)
        println("Workshop crudo desde API: $response")
        val workshopListInside = response.workshopList
        return workshopListInside.firstOrNull()?.toDomain()
    }

    /**
     * Post a list of all the data needed for creating a new workshop.
     *
     * Each item in the response is converted into a [Workshop] domain model.
     * All the data is established to have al the para meters of [Workshop]
     *
     * @return A new [Workshop] object representing a new generation of a workshop.
     */
    override suspend fun addWorkshop(newWorkshop: WorkshopDto): Workshop {
        val response = api.addWorkshop(newWorkshop)
        return Workshop(
            id = newWorkshop.id,
            idUser = newWorkshop.idUser,
            nameWorkshop = newWorkshop.name,
            url = newWorkshop.image,
            status = newWorkshop.status,
            description = newWorkshop.description,
            startHour = newWorkshop.startHour,
            finishHour = newWorkshop.finishHour,
            date = newWorkshop.date,
            videoTraining = newWorkshop.videoTraining
        )
    }

    /**
     * Retrieves the information for a specific workshop by its [name].
     *
     * This function calls the API to obtain the corresponding [WorkshopDto]
     * and converts it into a [Workshop] domain model using the [toDomain] mapper.
     *
     * @param name The name of the workshop to retrieve.
     * @return A [Workshop] object containing detailed workshop information.
     */
    override suspend fun searchWorkshop(name: String): List<Workshop> {
        val response = api.searchWorkshops(name)
        return response.map { it.toDomain() }
    }

    /**
     * Performs a soft delete of a workshop identified by its [id].
     *
     * This method builds the [DeleteWorkshopDto] request body and calls the
     * corresponding API endpoint (`workshop/delete`).
     *
     * @param id Unique identifier of the workshop to be soft-deleted.
     * @throws Exception If the network request fails or the server
     *         returns an error.
     */
    override suspend fun deleteWorkshops(id: String) {
        // Build the request body with the supply ID expected by the API
        val body = DeleteWorkshopDto(id)
        // If you want to check the status of an error
        // val body = DeleteWorkshopDto("i33")
        // Call the remote API to perform the soft delete operation
        val result = api.deleteWorkshops(body)
        Log.d("Validacion", "Si llego ")
    }

    override suspend fun modifyWorkshop(modifiedWorkshop: WorkshopDto): Workshop {
        val workshopId = modifiedWorkshop.id
        val response = api.modifyWorkshop(
            id = workshopId!!,
            requestBody = modifiedWorkshop
        )
        return Workshop(
            id = modifiedWorkshop.id,
            idUser = modifiedWorkshop.idUser,
            nameWorkshop = modifiedWorkshop.name,
            url = modifiedWorkshop.image,
            status = modifiedWorkshop.status,
            description = modifiedWorkshop.description,
            startHour = modifiedWorkshop.startHour,
            finishHour = modifiedWorkshop.finishHour,
            date = modifiedWorkshop.date,
            videoTraining = modifiedWorkshop.videoTraining
        )
    }

}