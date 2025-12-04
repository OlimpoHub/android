package com.app.arcabyolimpo.domain.repository.disability

import com.app.arcabyolimpo.domain.model.disabilities.Disability

interface DisabilityRepository {
    /**
     * Retrieves a list of all disabilities.
     *
     * @return A list of [Disability] domain models.
     */
    suspend fun getDisabilities(): List<Disability>

    /**
     * Creates a new disability entry.
     *
     * @param disability The [Disability] object containing the data for the new entry.
     */
    suspend fun registerDisability(disability: Disability)

    /**
     * Retrieves a single disability by its unique identifier.
     *
     * @param id The unique ID of the disability to fetch.
     * @return The corresponding [Disability] domain model.
     */
    suspend fun getDisability(id: String): Disability

    /**
     * Deletes a disability by its unique identifier.
     *
     * @param id The unique ID of the disability to delete.
     */
    suspend fun deleteDisability(id: String)
}
