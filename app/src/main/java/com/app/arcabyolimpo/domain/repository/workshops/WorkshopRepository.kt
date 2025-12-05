package com.app.arcabyolimpo.domain.repository.workshops

import com.app.arcabyolimpo.data.remote.dto.workshops.WorkshopDto
import com.app.arcabyolimpo.domain.model.workshops.Workshop

/**
 * Repository interface that defines all operations related to workshops.
 *
 * This abstraction is used by the domain layer to request data from
 * the remote source (API), while keeping the presentation layer completely
 * decoupled from networking and storage logic. Each function represents
 * a specific operation that can be performed on workshop data.
 */
interface WorkshopRepository {

    /**
     * Retrieves the complete list of workshops.
     *
     * This function allows the domain or presentation layer to request
     * all available workshops. The repository handles mapping from DTOs
     * to domain models.
     *
     * @return A list of [Workshop] representing all workshops.
     */
    suspend fun getWorkshopsList(): List<Workshop>

    /**
     * Retrieves detailed information for a single workshop by its ID.
     *
     * Used when the app needs to display or process detailed information
     * about a specific workshop.
     *
     * @param id Unique identifier of the workshop.
     * @return A [Workshop] instance if found, or `null` if not found.
     */
    suspend fun getWorkshopsById(id: String): Workshop?

    /**
     * Adds a new workshop to the backend.
     *
     * This function allows the domain or presentation layer to request
     * the creation of a new workshop. The repository transforms the
     * provided [WorkshopDto] into the required API format.
     *
     * @param newWorkshop DTO containing the data of the workshop to create.
     * @return The newly created [Workshop] model returned by the backend.
     */
    suspend fun addWorkshop(newWorkshop: WorkshopDto): Workshop

    /**
     * Modifies a single workshop.
     *
     * A simple function so that the domain or presentation layer
     * can request the modification of a workshop.
     *
     * @param modifiedWorkshop Data of the modified workshop in DTO format.
     * @return The updated [Workshop] instance returned by the API.
     */
    suspend fun modifyWorkshop(modifiedWorkshop: WorkshopDto): Workshop

    /**
     * Searches workshops by name.
     *
     * This function allows the domain or presentation layer to perform
     * partial-name matching to filter workshops.
     *
     * @param name Search keyword to match workshop names.
     * @return A list of [Workshop] that match the search criteria.
     */
    suspend fun searchWorkshop(name: String): List<Workshop>

    /**
     * Deletes a single workshop identified by its [id].
     *
     * A simple function so that the domain or presentation layer
     * can request the deletion of a workshop.
     *
     * @param id Unique identifier of the workshop to be deleted.
     */
    suspend fun deleteWorkshops(id: String)

    companion object
}
