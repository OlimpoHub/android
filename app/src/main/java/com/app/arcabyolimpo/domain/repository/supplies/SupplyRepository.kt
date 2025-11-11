package com.app.arcabyolimpo.domain.repository.supplies

import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.filter.FilterData
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt

/**
 * Retrieves detailed information for a specific supply by its [id].
 *
 * @param id The unique identifier of the supply.
 * @return A [Supply] object containing complete supply details.
 * @throws Exception If the supply cannot be found or fetched.
 */
interface SupplyRepository {
    suspend fun getSuppliesList(): List<Supply>

    suspend fun getSupplyById(id: String): Supply

    suspend fun registerSupplyBatch(batch: SupplyBatch): SupplyBatch
    
    suspend fun getSupplyBatchById(id: String): SupplyBatchExt

    suspend fun filterSupply(params: FilterDto): List<Supply>

    suspend fun getFilterData(): FilterData
}
