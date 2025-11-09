package com.app.arcabyolimpo.domain.repository.supplies

import androidx.navigation.NavType
import com.app.arcabyolimpo.data.remote.dto.supplies.FilterSuppliesDto
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import retrofit2.http.Body

interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    /**
     * Retrieves detailed information for a specific supply by its [id].
     *
     * @param id The unique identifier of the supply.
     * @return A [Supply] object containing complete supply details.
     * @throws Exception If the supply cannot be found or fetched.
     */
    suspend fun getSupplyById(id: String): Supply

    suspend fun filterSupply(filters: FilterSuppliesDto): List<Supply>

    suspend fun getFilterData(): FilterData
}
