package com.app.arcabyolimpo.data.repository.workshops

import com.app.arcabyolimpo.data.mapper.supplies.toDomain
import com.app.arcabyolimpo.data.mapper.workshops.toDomain
import com.app.arcabyolimpo.data.remote.api.ArcaApi
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

}