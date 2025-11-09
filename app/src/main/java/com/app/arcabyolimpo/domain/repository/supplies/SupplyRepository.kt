package com.app.arcabyolimpo.domain.repository.supplies

import com.app.arcabyolimpo.domain.model.supplies.Supply

/** Defines the contract for managing supply-related data operations. */
interface SupplyRepository {

    /**
     * Retrieves a list of all available supplies.
     *
     * @return A list of [Supply] objects containing basic supply information.
     * @throws Exception If the data retrieval fails.
     */
    suspend fun getSuppliesList(): List<Supply>

    /**
     * Retrieves detailed information for a specific supply by its [id].
     *
     * @param id The unique identifier of the supply.
     * @return A [Supply] object containing complete supply details.
     * @throws Exception If the supply cannot be found or fetched.
     */
    suspend fun getSupplyById(id: String): Supply
}
